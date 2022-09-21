package gxj.study.demo.flink.window;

import gxj.study.demo.flink.window.SlidingWindowAggregateSource.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/8/3 16:53
 */
public class SlidingWindowAggregateDemo {

    private static final String TO = "->";
    private static final Time SIZE = Time.seconds(2);

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 读取数据，并提取时间戳、
        DataStream<Event> stream = env.addSource(new SlidingWindowAggregateSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        }));
        //窗口大小5秒，滑动距离1秒，统计每个窗口内每个人的value之和\
        //使用默认时间trigger，窗口每滑动一次，触发一次fire，进行一次输出(getResult)
        DataStream<Tuple2<String, Integer>> aggregateStream = stream
                .keyBy(Event::getKey)
                .process(new KeyedProcessFunction<String, Event, Event>() {

                    private ValueStateDescriptor<Event> keyState = new ValueStateDescriptor<>("key", Event.class);

                    @Override
                    public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                        ValueState<Event> key = getRuntimeContext().getState(keyState);
                        if(key.value() == null){
                            Event sample = new Event();
                            sample.setKey(value.getKey());
                            sample.setWaterMark(true);
                            getRuntimeContext().getState(keyState).update(sample);
                        }

                        Long nextTimestamp = value.getTimestamp() - value.getTimestamp() % SIZE.toMilliseconds() + SIZE.toMilliseconds();

                        ctx.timerService().registerEventTimeTimer(nextTimestamp);
                        out.collect(value);
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Event> out) throws Exception {
                        Event waterMarkEvent = getRuntimeContext().getState(keyState).value();
                        Long nextTimestamp = timestamp - timestamp % SIZE.toMilliseconds() + SIZE.toMilliseconds();
                        ctx.timerService().registerEventTimeTimer(nextTimestamp);
                        waterMarkEvent.setTimestamp(timestamp);
                        out.collect(waterMarkEvent);
                    }
                })
                .keyBy(Event::getKey)
                // 设置滚动事件时间窗口
                .window(SlidingEventTimeWindows.of(SIZE, Time.seconds(1)))
                .aggregate(new AggregateFunction<Event, Tuple2<String, Integer>, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> createAccumulator() {
                        return Tuple2.of("", 0);
                    }

                    @Override
                    public Tuple2<String, Integer> add(Event value, Tuple2<String, Integer> accumulator) {
                        System.out.println("[add] accumulator:" + accumulator + " value:" + value);
                        if(value.isWaterMark()){
                            return Tuple2.of(value.getKey(), accumulator.f1);
                        }
                        return Tuple2.of(value.getKey(), accumulator.f1 + value.getValue());
                    }

                    @Override
                    public Tuple2<String, Integer> getResult(Tuple2<String, Integer> accumulator) {
                        return accumulator;
                    }

                    @Override
                    public Tuple2<String, Integer> merge(Tuple2<String, Integer> a, Tuple2<String, Integer> b) {
                        System.out.println("merge");
                        return Tuple2.of(b.f0, a.f1 + b.f1);
                    }
                });
        aggregateStream.print();
        env.execute();
    }

    private static String increase(String f0) {
        String[] split = f0.split(TO);
        int left = Math.max(0, Integer.valueOf(split[0]));
        int right = Integer.valueOf(split[1]) + 1;
        if (right - left >= 5) {
            left += 1;
        }
        return left + TO + right;
    }




}
