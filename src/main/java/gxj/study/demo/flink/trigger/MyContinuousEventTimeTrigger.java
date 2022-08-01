package gxj.study.demo.flink.trigger;

import com.alibaba.fastjson.JSON;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;


/**
 * @author xinjie_guo
 * @date 2022/7/3 13:16
 */
@PublicEvolving
public class MyContinuousEventTimeTrigger<W extends Window> extends Trigger<Object, W> {
    private static final long serialVersionUID = 1L;

    private final long interval;

    /**
     * When merging we take the lowest of all fire timestamps as the new fire timestamp.
     */
    private final ReducingStateDescriptor<Long> stateDesc =
            new ReducingStateDescriptor<>("fire-time", new MyContinuousEventTimeTrigger.Min(), LongSerializer.INSTANCE);

    private MyContinuousEventTimeTrigger(long interval) {
        this.interval = interval;
    }

    @Override
    public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx) throws Exception {
        System.out.println("【trigger】 onElement: " + JSON.toJSONString(element));
        if (window.maxTimestamp() <= ctx.getCurrentWatermark()) {
            // if the watermark is already past the window fire immediately
            return TriggerResult.FIRE;
        } else {
            ctx.registerEventTimeTimer(window.maxTimestamp());
        }

        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
        if (fireTimestamp.get() == null) {
            long start = timestamp - (timestamp % interval);
            long nextFireTimestamp = start + interval;
            ctx.registerEventTimeTimer(nextFireTimestamp);
            fireTimestamp.add(nextFireTimestamp);
        }

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
        System.out.println("【trigger】 onEventTime: " +time);
        if (time == window.maxTimestamp()) {
            System.out.println("【trigger】 onEventTime -> FIRE!");
            return TriggerResult.FIRE;
        }

        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
        if (fireTimestamp.get().equals(time)) {
            fireTimestamp.clear();
            fireTimestamp.add(time + interval);
            ctx.registerEventTimeTimer(time + interval);
            System.out.println("【trigger】 onEventTime -> FIRE!");
            return TriggerResult.FIRE;
        }

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(W window, TriggerContext ctx) throws Exception {
        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
        Long timestamp = fireTimestamp.get();
        if (timestamp != null) {
            ctx.deleteEventTimeTimer(timestamp);
            fireTimestamp.clear();
        }
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void onMerge(W window, OnMergeContext ctx) throws Exception {
        ctx.mergePartitionedState(stateDesc);
        Long nextFireTimestamp = ctx.getPartitionedState(stateDesc).get();
        if (nextFireTimestamp != null) {
            ctx.registerEventTimeTimer(nextFireTimestamp);
        }
    }

    @Override
    public String toString() {
        return "MyContinuousEventTimeTrigger(" + interval + ")";
    }

    @VisibleForTesting
    public long getInterval() {
        return interval;
    }

    /**
     * Creates a trigger that continuously fires based on the given interval.
     *
     * @param interval The time interval at which to fire.
     * @param <W>      The type of {@link Window Windows} on which this trigger can operate.
     */
    public static <W extends Window> MyContinuousEventTimeTrigger<W> of(Time interval) {
        return new MyContinuousEventTimeTrigger<>(interval.toMilliseconds());
    }

    private static class Min implements ReduceFunction<Long> {
        private static final long serialVersionUID = 1L;

        @Override
        public Long reduce(Long value1, Long value2) throws Exception {
            return Math.min(value1, value2);
        }
    }
}
