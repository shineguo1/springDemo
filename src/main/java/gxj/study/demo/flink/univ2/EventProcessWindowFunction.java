package gxj.study.demo.flink.univ2;

import com.google.common.collect.Lists;
import gxj.study.demo.flink.univ2.model.CalcAction;
import gxj.study.demo.flink.univ2.model.EventData;
import gxj.study.demo.flink.univ2.model.Pair;
import gxj.study.demo.flink.univ2.statemachine.Machine;
import gxj.study.demo.flink.univ2.utils.BatchReq;
import gxj.study.demo.flink.univ2.utils.BatchUtils;
import gxj.study.demo.flink.univ2.utils.CollectionUtils;
import gxj.study.demo.flink.univ2.utils.FunctionConstant;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/29 14:44
 */
public class EventProcessWindowFunction<KEY> extends ProcessWindowFunction<EventData, CalcAction, KEY, TimeWindow> {

    private ValueStateDescriptor<Pair> pairState = new ValueStateDescriptor<>("pair", Pair.class);

    {
        //设置失效时间 - 可选：setUpdateType更新类型，setStateVisibility可见性等属性
        pairState.enableTimeToLive(StateTtlConfig.newBuilder(Time.days(7)).build());
    }

    /**
     * @param key      Long timestamp 精确到秒
     * @param context  窗口上下文
     * @param elements 窗口内元素迭代器
     * @param out      输出
     */
    @Override
    public void process(KEY key, Context context, Iterable<EventData> elements, Collector<CalcAction> out) throws Exception {

        Pair pair = createPairIfAbsent(context, String.valueOf(key));

        //1. 按交易hash分组
        Map<String, List<EventData>> txHashMap = StreamSupport.stream(elements.spliterator(), false)
                //按交易hash分组
                .collect(Collectors.groupingBy(o -> o.getData().getString("txHash")));
        //2. 交易hash分组处理
        txHashMap.values().forEach(list -> process(context, list, out, pair));
    }

    private Pair createPairIfAbsent(Context context, String pairAddress) throws IOException {
        ValueState<Pair> pair = context.windowState().getState(pairState);
        if (pair.value() == null) {
            Pair p = getPair(pairAddress);
            pair.update(p);
            return p;
        }
        return pair.value();
    }

    private Pair getPair(String pairAddress) {
        List<BatchReq> reqList = Lists.newArrayList(
                new BatchReq(pairAddress, FunctionConstant.TOKEN0),
                new BatchReq(pairAddress, FunctionConstant.TOKEN1)
        );
        List<Object> res1 = BatchUtils.batch(reqList, DefaultBlockParameterName.LATEST);

        List<BatchReq> reqList2 = Lists.newArrayList(
                new BatchReq(res1.get(1).toString(), FunctionConstant.DECIMALS),
                new BatchReq(res1.get(2).toString(), FunctionConstant.DECIMALS)
        );
        List<Object> res2 = BatchUtils.batch(reqList2, DefaultBlockParameterName.LATEST);
        Pair pair = new Pair();
        pair.setToken0(String.valueOf(res1.get(0)).toLowerCase());
        pair.setToken1(String.valueOf(res1.get(1)).toLowerCase());
        pair.setDecimals0(Integer.valueOf(res2.get(0).toString()));
        pair.setDecimals0(Integer.valueOf(res2.get(0).toString()));
        return pair;
    }

    private void process(Context context, List<EventData> list, Collector<CalcAction> out, Pair pair) {
        list.sort(this::compare);
        String currentAction = "";

        CalcAction calcAction = new CalcAction(pair);

        //创建状态机
        Machine machine = Machine.create(context, out, pair);
        while (list.size() > 0) {
            EventData eventData = CollectionUtils.pollFirst(list);
            //data is notNull, sourceStream那里filter过。
            machine.accept(eventData);
        }
    }


    private int compare(EventData o1, EventData o2) {
        Long blockNo1 = o1.getData().getLong("blockNo");
        Long blockNo2 = o2.getData().getLong("blockNo");
        Long txIndex1 = o1.getData().getLong("transactionIndex");
        Long txIndex2 = o2.getData().getLong("transactionIndex");
        long ret = !blockNo1.equals(blockNo2) ? blockNo1 - blockNo2 : txIndex1 - txIndex2;
        return ret > 0 ? 1 : (ret < 0 ? -1 : 0);
    }


}
