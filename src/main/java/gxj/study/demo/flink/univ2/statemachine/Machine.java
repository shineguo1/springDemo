package gxj.study.demo.flink.univ2.statemachine;

import gxj.study.demo.flink.univ2.model.CalcAction;
import gxj.study.demo.flink.univ2.model.EventData;
import gxj.study.demo.flink.univ2.model.Pair;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.util.Collector;

import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:14
 */
@Data
public class Machine {

    private StateEnum state;
    private EventData cacheEvent;
    private CalcAction cacheAction;
    private ProcessWindowFunction.Context context;
    private Collector<CalcAction> out;
    private Pair pair;

    private Machine(ProcessWindowFunction.Context context, Collector<CalcAction> out, Pair pair) {
        this.state = StateEnum.init;
        this.cacheEvent = null;
        this.context = context;
        this.out = out;
        this.pair = pair;
    }

    public static Machine create(ProcessWindowFunction.Context context, Collector<CalcAction> out, Pair pair) {
        return new Machine(context, out, pair);
    }

    /**
     * 状体转换表
     * （ 符号说明：
     * 1. - 表示状态不变，continue；
     * 2. × 表示接收到了不应该出现的事件，说明数据有差错，跳过当前这组action，以init状态重新开始接收事件。
     * ）
     * <p>
     * 状态\事件            transfer(Transfer)  transfer(Mint)          transfer(Burn)          mint    burn        other
     * init               transfer            transferOfMint          transferOfBurn           -       -           -
     * transferOfMint     transfer(×)         transferOfMint(×)       transferOfBurn(×)       mint     init(×)     init(×)
     * transferOfBurn     transfer(×)         transferOfMint(×)       transferOfBurn(×)        -       burn        -
     * transfer(同init)    transfer            transferOfMint          transferOfBurn          init    init       init
     * mint(同init)        transfer            transferOfMint          transferOfBurn          init    init       init
     * burn(同init)        transfer            transferOfMint          transferOfBurn          init    init       init
     */

    public StateEnum accept(EventData eventData) {
        state = state.getOperator().accept(eventData, this);
        return state;
    }


    protected void clearCache() {
        cacheEvent = null;
        cacheAction = null;
    }
}
