package gxj.study.demo.flink.window;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/30 16:26
 */
@Data
@PublicEvolving
public class MyTrigger extends Trigger<Object, TimeWindow> {

    private ValueStateDescriptor<Boolean> isFirstElement = new ValueStateDescriptor<>("isFirstElement", Boolean.class);

    @Override
    public TriggerResult onElement(
            Object element, long timestamp, TimeWindow window, TriggerContext ctx)
            throws Exception {
//        System.out.println("[trigger] on element, ctx" + ctx);

        if (window.getStart() == ctx.getCurrentWatermark()) {
            System.out.println("[trigger] window is start —— judge by Watermark");
        }
        if (ctx.getPartitionedState(isFirstElement).value() == null) {
            ctx.getPartitionedState(isFirstElement).update(true);
            System.out.println("[trigger] window is start —— judge by Watermark");
        }
        System.out.println("[trigger] ctx.getCurrentWatermark:" + new Date(ctx.getCurrentWatermark()));
        if (window.maxTimestamp() <= ctx.getCurrentWatermark()) {
            System.out.println("[trigger] window is close");
            // if the watermark is already past the window fire immediately
            return TriggerResult.FIRE;
        } else {
            System.out.println("[trigger] window.maxTimestamp:" + new Date(window.maxTimestamp()));
            ctx.registerEventTimeTimer(window.maxTimestamp());
            return TriggerResult.CONTINUE;
        }
    }


    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        System.out.println("trigger clear");
        ctx.getPartitionedState(isFirstElement).clear();
        ctx.deleteEventTimeTimer(window.maxTimestamp());
    }


    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
        System.out.println("[trigger] onEventTime");
        return time == window.maxTimestamp() ? TriggerResult.FIRE : TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx)
            throws Exception {
        return TriggerResult.CONTINUE;
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
        if (windowMaxTimestamp > ctx.getCurrentWatermark()) {
            ctx.registerEventTimeTimer(windowMaxTimestamp);
        }
    }

    @Override
    public String toString() {
        return "MyTrigger()";
    }

    public static MyTrigger create() {
        return new MyTrigger();
    }
}
