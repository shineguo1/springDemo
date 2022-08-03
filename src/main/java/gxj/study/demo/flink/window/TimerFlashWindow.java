package gxj.study.demo.flink.window;

import com.alibaba.fastjson.JSONObject;
import com.clearspring.analytics.util.Lists;
import gxj.study.util.TimeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/25 14:11
 */
public class TimerFlashWindow extends ProcessWindowFunction<JSONObject, JSONObject, String, TimeWindow> {

    private String key = "";
    private ValueStateDescriptor<Long> lastElementHourState = new ValueStateDescriptor<>("lastElementHour", Long.TYPE);
    private ValueStateDescriptor<Long> hourTotalValueState = new ValueStateDescriptor<>("hourTotalValue", Long.TYPE);
    private ValueStateDescriptor<Long> lastHourTotalValueState = new ValueStateDescriptor<>("lastHourTotalValue", Long.TYPE);


    @Override
    public void open(Configuration parameters) throws Exception {
        System.out.println("new window. key: " + parameters.keySet());
    }

    @Override
    public void close() throws Exception {
        System.out.println("window close. key: " + key);
    }

    @Override
    public void process(String s, Context context, Iterable<JSONObject> elements, Collector<JSONObject> out) throws Exception {
        key = s;
        long watermark = context.currentWatermark();
//        System.out.println(this + "in process, key:" + s + " water mark:" + watermark);

        //接受元素
        Iterator<JSONObject> iterator = elements.iterator();
        List<JSONObject> objects = Lists.newArrayList();
        while (iterator.hasNext()) {
            JSONObject o = iterator.next();
            if (isAccept(o)) {
                objects.add(o);
                iterator.remove();
            }
        }

        //业务计算
        /*
        * 输入数据格式：
        *  "{\"key\":\"abc\",\"time\":\"1100\",\"snapshot\":0,\"value\":1}"
        *
        * 简化需求：
        * 1. 对每小时的数据做快照，每条数据包含上一小时的value汇总和当前小时的value汇总
        * 2. 当前小时，快照数据随时间实时更新（1:10时，统计1:00-1:10的数据；1:30时，统计1:00-1:30的数据）
        * 3. 当前小时若无数据，仍要有快照（即有一条value汇总 = 0的初始数据）
        *
        * ======== [失败]思路1，由于找不到方法把“trigger触发的时间戳”传入窗口计算函数 ========
        * 每小时记录一次快照。思路是用触发器触发计算，如果刚好是一小时整，就快照入库。
        * 假如窗口大小2小时，每2分钟FIRE一次触发计算，三种情况：
        * 1.窗口水位线到了快照时间，窗口内没有元素，需要拍摄快照入库
        * 2.窗口水位线到了快照时间，窗口内有元素，元素时间未超过快照时间，处理完所有元素后快照入库。
        * 3.窗口水位线到了快照时间，窗口内有元素，元素来的太密集，以至于窗口内元素已经超过了快照时间，处理元素时依据元素时间快照入库并更新快照时间。处理完后窗口水位线没有到达新的快照时间，不再做多余的快照。
        *
        * 问题：
        * 上下文环境水位线推进的比触发窗口的水位线快的多，窗口函数只能拿到环境水位线，拿不到窗口水位线，会导致以下问题
        * 1. 触发计算时，窗口内没元素，怎么知道现在是哪个时间（窗口水位线）？这影响到判断当前时间是不是需要做快照。
        * 2. 触发窗口的水位线已经到达快照时间，窗口内元素未到快照时间，如何界定需要快照？
        * 3. 如果换种思路，等下一小时的首个元素到达，来触发快照落库动作，那么假设2个元素之间间隔了3个小时，那么这中间的2个小时快照就会延迟2个小时落库。
        * 4. 如果发现窗口元素是空的，可以将快照追到环境水位线吗？不可以，因为元素可能没有进窗口（窗口时间小于环境时间）。
        *
        * ======== [成功]思路2，窗口对每一条数据做计算工作，下一个算子再对计算结果补充快照 ========
        * [1HOUR]->INIT_SNAPSHOT->ELEMENT->ELEMENT->...->[2HOUR]->INIT_SNAPSHOT->...
        *
        */
        List<JSONObject> sortedElements = objects.stream().sorted(this::sort).collect(Collectors.toList());
        for (JSONObject o : sortedElements) {
            //元素时间
            Long elementTimestamp = o.getLong("time");
            //上个元素所在整点（小时快照的时间，即整点）
            Long hourSnapshot = getLastElementHour(context, TimeUtils.getLastHour(elementTimestamp));

            //step1: 判断小时区间，更新缓存数据
            //如果元素时间大于等于上一个元素的整点，说明上一个小时结束，进入当前小时。此时调整state缓存的值。
            //注意：上一条数据的小时和当前小时不一定是连续的，两条数据之间是允许间隔几个小时没有数据的！
            if (elementTimestamp >= hourSnapshot) {
                //如果恰好是下一个小时，当前小时缓存的totalValue计入上一小时
                if (elementTimestamp - hourSnapshot < TimeUtils.ONE_HOUR) {
                    Long lastHourValue = context.globalState().getState(hourTotalValueState).value();
                    lastHourValue = ObjectUtils.defaultIfNull(lastHourValue, 0L);
                    context.globalState().getState(lastHourTotalValueState).update(lastHourValue);
                }
                //否则，上一个小时的汇总计为零（间隔2个小时以上，说明上个小时区间没数据）
                else {
                    context.globalState().getState(lastHourTotalValueState).update(0L);
                }
                //当前小时缓存的totalValue清零
                context.globalState().getState(hourTotalValueState).update(0L);
                context.globalState().getState(lastElementHourState).update(TimeUtils.getCurrentHour(elementTimestamp));
            }

            //step2：当前小时区间进行增量计算
            //统计当前小时的totalValue
            Long hourTotalValue = context.globalState().getState(hourTotalValueState).value();
            Long lastHourTotalValue = context.globalState().getState(lastHourTotalValueState).value();
            hourTotalValue += o.getLong("value");
            context.globalState().getState(hourTotalValueState).update(hourTotalValue);

            //step3：更新当前小时区间快照结果
            //构造返回对象
            o.put("snapHour", TimeUtils.getCurrentHour(elementTimestamp));
            o.put("totalValue", hourTotalValue);
            o.put("lastTotalValue", lastHourTotalValue);
            out.collect(o);
        }
    }

    private Long getLastElementHour(Context context, Long defaultTime) throws Exception {
        Long lastHour = context.globalState().getState(this.lastElementHourState).value();
        if (lastHour == null) {
            lastHour = defaultTime;
            context.globalState().getState(this.lastElementHourState).update(defaultTime);
        }
        return lastHour;
    }

    private int sort(JSONObject o1, JSONObject o2) {
        return Long.compare(o1.getLong("time"), o2.getLong("time"));
    }


    /**
     * 元素O是否需要被接收计算
     */
    private boolean isAccept(JSONObject o) {
        return true;
    }

}
