package gxj.study.demo.flink.window;

import akka.event.EventStream;
import com.alibaba.fastjson.JSON;
import gxj.study.demo.flink.univ2.model.EventData;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

/**
 * 测试窗口生命周期结束，销毁过程会回调什么方法
 *
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/30 17:22
 */
public class WindowLifeCycleDemo {


    private static final String BOOTSTRAP_SERVER = "10.0.50.121:9092,10.0.50.122:9092,10.0.50.127:9092";

    public static void main(String[] args) throws Exception {

        /* ================ 1. 创建table环境 ================ */

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);


         /* ================ 2. 连接socket数据源 ================ */

        DataStream<EventData> eventStream = env.socketTextStream("127.0.0.1", 8888)
                .filter(StringUtils::isNotBlank)
                .map((MapFunction<String, EventData>) o -> JSON.parseObject(o, EventData.class));


        /* ================ 3. 分组分窗口 ================ */
        eventStream
                .filter(o -> Objects.nonNull(o.getData()) && StringUtils.isNotBlank(o.getData().getString("pairAddress")))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<EventData>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                                .withTimestampAssigner(getSerializableTimestampAssigner()))
                .keyBy(o -> o.getData().getString("pairAddress"))
//                .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .trigger(MyTrigger.create())
                //清理计算过的数据
//                .evictor(new TimeEvictor<TimeWindow>(1))
                .process(new EventProcessWindowFunction<>())
                .addSink(new PrintSinkFunction<>())
        ;
        DataStream<EventData> eventStream1 = eventStream;


        //7.1 输出：命令行print
        env.execute();

    }

    private static SerializableTimestampAssigner<EventData> getSerializableTimestampAssigner() {
        return (SerializableTimestampAssigner<EventData>) (element, recordTimestamp) -> element.getData().getLong("currentTimestamp")*1000;
    }

}
