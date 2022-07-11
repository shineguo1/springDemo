package gxj.study.demo.flink.trigger;

import com.alibaba.fastjson.JSON;
import gxj.study.demo.flink.common.model.EventData;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/1 10:58
 */
@SuppressWarnings("all")
public class WindowTriggerExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<EventData> eventStream = env.socketTextStream("127.0.0.1", 8888)
                .filter(StringUtils::isNotBlank)
                .map((MapFunction<String, EventData>) o -> JSON.parseObject(o, EventData.class));

        eventStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<EventData>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                        .withTimestampAssigner(new SerializableTimestampAssigner<EventData>() {
                            @Override
                            public long extractTimestamp(EventData event, long l) {
                                return event.getData().getLong("currentTimestamp");
                            }
                        }))
                .keyBy(o -> o.getProject())
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
//                .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
//                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5)))
                .trigger(new MyTrigger())
                .process(new WindowResult())
                .print();
        env.execute();
    }

    public static class WindowResult extends ProcessWindowFunction<EventData,
            EventData, String, TimeWindow> {
        @Override
        public void process(String s, Context context, Iterable<EventData> iterable,
                            Collector<EventData> collector) throws Exception {
            System.out.println("[window function] in process");
            collector.collect(iterable.iterator().next());
        }
    }

    public static class MyTrigger extends Trigger<EventData, TimeWindow> {
        @Override
        public TriggerResult onElement(EventData event, long l, TimeWindow timeWindow,
                                       TriggerContext triggerContext) throws Exception {
            System.out.println("[trigger] onElement");
            ValueState<Boolean> isFirstEvent =
                    triggerContext.getPartitionedState(
                            new ValueStateDescriptor<Boolean>("first-event",
                                    Types.BOOLEAN)
                    );
            System.out.println("[trigger] ctx.watermark" + new Date(triggerContext.getCurrentWatermark()));
            if (isFirstEvent.value() == null) {
                for (long i = timeWindow.getStart(); i < timeWindow.getEnd(); i =
                        i + 1000L) {
                    System.out.println("[trigger] register time:" + new Date(i));
                    triggerContext.registerEventTimeTimer(i);
                }
                isFirstEvent.update(true);
            }
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onEventTime(long l, TimeWindow timeWindow,
                                         TriggerContext triggerContext) throws Exception {
            System.out.println("[trigger] onEventTime");
            return TriggerResult.FIRE;
        }

        @Override
        public TriggerResult onProcessingTime(long l, TimeWindow timeWindow,
                                              TriggerContext triggerContext) throws Exception {
            System.out.println("[trigger] onProcessingTime");
            return TriggerResult.CONTINUE;
        }

        @Override
        public void clear(TimeWindow timeWindow, TriggerContext triggerContext)
                throws Exception {
            System.out.println("[trigger] clear");
            ValueState<Boolean> isFirstEvent =
                    triggerContext.getPartitionedState(
                            new ValueStateDescriptor<Boolean>("first-event",
                                    Types.BOOLEAN)
                    );
            isFirstEvent.clear();
        }


        @Override
        public boolean canMerge() {
            return true;
        }

        @Override
        public void onMerge(TimeWindow window, OnMergeContext ctx) {
            // only register a timer if the watermark is not yet past the end of the merged window
            // this is in line with the logic in onElement(). If the watermark is past the end of
            // the window onElement() will fire and setting a timer here would fire the window twice.
            long windowMaxTimestamp = window.maxTimestamp();
            System.out.println("[trigger] onMerge， windowMaxTimestamp：" + windowMaxTimestamp);
            if (windowMaxTimestamp > ctx.getCurrentWatermark()) {
                ctx.registerEventTimeTimer(windowMaxTimestamp);
            }
        }
    }
}