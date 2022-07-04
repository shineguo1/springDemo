package gxj.study.demo.flink.window;

import gxj.study.demo.flink.univ2.model.EventData;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/29 14:44
 */
public class EventProcessWindowFunction<KEY> extends ProcessWindowFunction<EventData, EventData, KEY, TimeWindow> {

    private ListStateDescriptor<EventData> dataStateDescriptor = new ListStateDescriptor<>("data", EventData.class);

    {
        //设置失效时间 - 可选：setUpdateType更新类型，setStateVisibility可见性等属性
        dataStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.hours(1)).build());
    }

    /**
     * @param key      Long timestamp 精确到秒
     * @param context  窗口上下文
     * @param elements 窗口内元素迭代器
     * @param out      输出
     */
    @Override
    public void process(KEY key, Context context, Iterable<EventData> elements, Collector<EventData> out) throws Exception {
        TimeWindow window = context.window();

        System.out.println("do process");
        long now = System.currentTimeMillis();
        if (now >= window.getEnd() - 1) {
            System.out.println("window will close: now:" + now + " end:" + window.getEnd());
        }
        elements.forEach(out::collect);
    }

}
