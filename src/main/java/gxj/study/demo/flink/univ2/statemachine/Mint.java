package gxj.study.demo.flink.univ2.statemachine;

import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.univ2.model.CalcAction;
import gxj.study.demo.flink.univ2.model.EventData;
import gxj.study.demo.flink.univ2.model.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:03
 */
public class Mint implements State {
    @Override
    public StateEnum accept(EventData eventData, Machine machine) {
        return StateEnum.init.getOperator().accept(eventData, machine);
    }

    /**
     * amount0 = mint.amount0/(10 ** decimals0),
     * amount1 = mint.amount1/(10 ** decimals1),
     * sender = mint.sender,
     * logindex = mint.logindex,
     */
    @Override
    public void doAction(Machine machine, EventData eventData) {
        CalcAction cacheAction = machine.getCacheAction();
        if (Objects.isNull(cacheAction)) {
            return;
        }
        JSONObject data = eventData.getData();
        Pair pair = machine.getPair();
        cacheAction.setAmount0(data.getBigDecimal("amount0").divide(BigDecimal.valueOf(Math.pow(10, pair.getDecimals0())), pair.getDecimals0(), RoundingMode.HALF_UP));
        cacheAction.setAmount1(data.getBigDecimal("amount1").divide(BigDecimal.valueOf(Math.pow(10, pair.getDecimals1())), pair.getDecimals1(), RoundingMode.HALF_UP));
        cacheAction.setSender(data.getString("sender"));
        cacheAction.setLogIndex(data.getInteger("logIndex"));

        //输出结果，清楚状态机缓存。
        machine.getOut().collect(cacheAction);
        machine.clearCache();
    }
}
