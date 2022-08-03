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
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.io.IOException;
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
//                System.out.println("[timestampAssigner] element时间戳：" + time);
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
                .window(TumblingEventTimeWindows.of(Time.milliseconds(500)))
                .trigger(MyContinuousEventTimeTrigger.of(Time.milliseconds(100)))
                .process(new TimerFlashWindow());

        //step2：这里给输出流添加初始快照（在每小时开始添加INIT_SNAPSHOT，目的是解决连续一个小时以上没数据，导致缺少那几个小时的快照）
        set = set.keyBy(o -> o.getString("key")).process(new KeyedProcessFunction<String, JSONObject, JSONObject>() {
            private ValueStateDescriptor<JSONObject> lastValue = new ValueStateDescriptor<>("keyedLastValue", JSONObject.class);

            @Override
            public void processElement(JSONObject value, Context ctx, Collector<JSONObject> out) throws Exception {
                Long time = value.getLong("time");
                TimerService timerService = ctx.timerService();
                /*
                 [失败方案]这里尝试破坏封装，在处理每个消息时，都提前向timeService传递水位线，结果是只能影响到当前key，不能广播到全部key。
                代码如下：
                InternalTimerServiceImpl internalTimerService = (InternalTimerServiceImpl) ReflectionUtils.getFieldValue(timerService, "internalTimerService");
                internalTimerService.advanceWatermark(time);
                */
                //更新状态
                JSONObject last = getRuntimeContext().getState(lastValue).value();
                if (last != null) {
                    //上一条数据的快照小时
                    Long lastSnapHour = last.getLong("snapHour");
                    //当前的快照小时
                    Long currentSnapHour = value.getLong("snapHour");
                    //如果跨了小时区间，插入快照
                    if (currentSnapHour > lastSnapHour) {
                        System.out.print("[process]");
                        collectSnapBetween(currentSnapHour, last, out);
                    }
                }
                getRuntimeContext().getState(lastValue).update(value);
                //注册下一个整点的Timer
                ctx.timerService().registerEventTimeTimer(TimeUtils.getCurrentHour(time));
                //输出流
                System.out.print("[process] timer watermark:" + timerService.currentWatermark() + "    ");
                System.out.println("[process]" + JSON.toJSONString(value));
                out.collect(value);
            }

            private void collectSnapBetween(Long currentSnapHour, JSONObject lastObj, Collector<JSONObject> out) throws IOException {
                //上一条数据的快照小时
                Long lastSnapHour = lastObj.getLong("snapHour");
                //当前的快照小时
                for (long time = lastSnapHour; time < currentSnapHour; time += TimeUtils.ONE_HOUR) {
                    Long totalValue = lastObj.getLong("totalValue");
                    lastObj.put("time", time);
                    lastObj.put("snapHour", TimeUtils.getCurrentHour(time));
                    lastObj.put("lastTotalValue", totalValue);
                    lastObj.put("totalValue", 0L);
                    System.out.println("[snapshot]" + JSON.toJSONString(lastObj));
                    out.collect(JSONObject.parseObject(lastObj.toJSONString()));
                }
                //更新状态
                getRuntimeContext().getState(lastValue).update(lastObj);
            }

            /**
             * 自这个key有数据开始，每一个整点触发
             *
             * 问题：
             * 现有一个20000millis的定时器和一条20001millis的数据。数据来的时候，
             * - 是先推动水位线处理定时器Timer，再处理数据ProcessEvent？
             * - 还是在处理数据ProcessEvent的时候推动水位线，所以处理完数据再处理定时器Timer？
             *
             * 观察到的现象：
             * - 对于新的keyed第一条消息来说，会先advanceWatermark（触发timer）再processElement
             * - 对于旧的keyed的消息来说，会先processElement，然后如果发现能触发timer，就更新水位线。
             * - 并非一定要达到注册的timer时间，processElement的过程中也存在推动timeService水位线的现象。
             *
             * 探索过程：
             * -【已知】KeyedProcessOperator算子输入流(即StreamInputProcessor读取到的消息)：是记录与水位线混合的流 Record->Record->WaterMark->Record ...
             * -【已知】Operator算子通过内部定时器(internalTimerService)的advanceWatermark(long waterMark)方法向timerService传递水位线。internalTimerService移除内部低于水位线的timer，并回调triggerTarget的onEventTime事件。
             * -【尝试】 如果在KeyedProcessFunction内部，每次processElement的时候，手动推动timeService水位线，去触发onTimer回调，是不是就能控制住顺序了呢？
             * -【失败】通过类反射破坏TimeService的封装，强行向internalTimerService传递消息的水位线，将只会传递给当前keyed的timer，无法广播到其他keyed的timer。
             *
             * 依旧不明白:
             * -【已知】StreamInputProcessor(StreamOneInputProcessor)做了2件事。一是消息解序列化并交由算子处理；二是追踪 Watermark Event 并分发时间对齐事件。
             * -【已知】StreamOneInputProcessor 从内存(NonSpanningWrapper)或磁盘(SpanningWrapper)读取下一个数据。 代码在 AbstractStreamTaskNetworkInput.java:95，即、SpillingAdaptiveSpanningRecordDeserializer#getNextRecord方法。
             * -【疑问】现在的问题是内存(NonSpanningWrapper)或磁盘(SpanningWrapper)里的数据，为什么有些是Record，有些是WaterMark？WaterMark是从哪来的，为什么它与Record不是一一对应的？为什么选取这些时间生成WaterMark？
             *
             * 笨笨的解决方案 - “谁先运行谁处理”：
             * - 遇到消息跨小时区间时，先回调onTimer还是processElement不确定，那么在2个回调方法中做同一个逻辑“如果发现跨小时区间，补充小时初始快照”。2个回调方法谁先运行谁处理。
             * - 因为当一个回调方法补充小时初始快照之后，第二个回调方法运行时会发现消息和上一条消息(缓存)已经在同一个小时区间了。
             */
            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<JSONObject> out) throws Exception {
                //注册下一个整点的Timer
                long currentSnapHour = TimeUtils.getCurrentHour(timestamp);
                ctx.timerService().registerEventTimeTimer(currentSnapHour);
                //状态存放上一次快照数据，到整点后，根据上一小时最后一条数据（即最新快照缓存）初始化当前整点的快照
                System.out.print("[timer] timer watermark:" + ctx.timerService().currentWatermark() + "    ");
                //上条记录
                JSONObject value = getRuntimeContext().getState(lastValue).value();
                //初始化过程：如果跨了小时区间，插入快照
                if (currentSnapHour > value.getLong("snapHour")) {
                    System.out.print("[timer]");
                    collectSnapBetween(currentSnapHour, value, out);
                }else{
                    System.out.println("");
                }
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