package gxj.study.demo.flink.window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.trigger.MyContinuousEventTimeTrigger;
import gxj.study.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.function.Function;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 9:54
 */
public class TimerFlashDemo {

    public static void main(String[] args) throws Exception {
        //无边界 - 流式 ： socketStream 启动 SocketServer 发送数据
        execute(TimerFlashDemo::unboundedSource);
    }

    public static void execute(Function<StreamExecutionEnvironment, DataStream<String>> f) throws Exception {
        //1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        /*
         水位线TimestampAssigner
         */
        SerializableTimestampAssigner<String> timestampAssigner = new SerializableTimestampAssigner<String>() {
            @Override
            public long extractTimestamp(String s, long l) {
                Long time = JSON.parseObject(s).getLong("time");
                System.out.println("[timestampAssigner] element时间戳：" + time);
                return time;
            }
        };

        //2. 读取数据
        DataStream<String> stringDataSource = f.apply(env);
        SingleOutputStreamOperator<String> source = stringDataSource
                .filter(StringUtils::isNotEmpty)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                        .withTimestampAssigner(timestampAssigner));

        //3. DataSet 流处理
        //需求：记录数据的小时快照，如果一个小时内有连续数据，快照要能实时更新。如果一个小时内没有数据，要有零数据的快照。
        //思路：在每个小时初生成初始快照，后续有元素进入实时更新当前时间(小时)的快照
        //[1HOUR]->INIT_SNAPSHOT->ELEMENT->ELEMENT->...->[2HOUR]->INIT_SNAPSHOT->...
        //step1：这里输出element
        DataStream<JSONObject> set = source
                .map(JSON::parseObject)
                .keyBy(o -> o.getString("key"))
                .window(TumblingEventTimeWindows.of(Time.milliseconds(5000)))
                .trigger(MyContinuousEventTimeTrigger.of(Time.milliseconds(1000)))
                .process(new TimerFlashWindow());

        //step2：这里给输出流添加初始快照（在每小时开始添加INIT_SNAPSHOT，目的是解决连续一个小时以上没数据，导致缺少那几个小时的快照）
        set = set.keyBy(o -> o.getString("key")).process(new KeyedProcessFunction<String, JSONObject, JSONObject>() {
            private ValueStateDescriptor<JSONObject> lastValue = new ValueStateDescriptor<>("keyedLastValue", JSONObject.class);

            @Override
            public void processElement(JSONObject value, Context ctx, Collector<JSONObject> out) throws Exception {
                Long time = value.getLong("time");
                //更新状态
                getRuntimeContext().getState(lastValue).update(value);
                //注册下一个整点的Timer
                ctx.timerService().registerEventTimeTimer(TimeUtils.getCurrentHour(time));
                //输出流
                System.out.println("[process]"+JSON.toJSONString(value));
                out.collect(value);
            }

            /**
             * 自这个key有数据开始，每一个整点触发
             */
            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<JSONObject> out) throws Exception {
                //注册下一个整点的Timer
                ctx.timerService().registerEventTimeTimer(TimeUtils.getCurrentHour(timestamp));
                //状态存放上一次快照数据，到整点后，根据上一小时最后一条数据（即最新快照缓存）初始化当前整点的快照
                //初始化过程：
                JSONObject value = getRuntimeContext().getState(lastValue).value();
                Long totalValue = value.getLong("totalValue");
                value.put("time", timestamp);
                value.put("snapHour", TimeUtils.getCurrentHour(timestamp));
                value.put("lastTotalValue", totalValue);
                value.put("totalValue", 0L);
                //更新状态
                getRuntimeContext().getState(lastValue).update(value);
                //输出流
                System.out.println("[timer]"+JSON.toJSONString(value));
                out.collect(value);
            }
        });
        //4.sink输出，这里使用打印输出，同 set.print()
//        set.addSink(new PrintSinkFunction<>());

        //5. 启动执行
        env.execute("HourSnapshot_Timer");
    }

    private static DataStreamSource<String> unboundedSource(StreamExecutionEnvironment env) {
        return env.socketTextStream("127.0.0.1", 8888);
    }

}