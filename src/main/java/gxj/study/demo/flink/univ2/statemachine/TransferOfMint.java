package gxj.study.demo.flink.univ2.statemachine;

import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.univ2.model.CalcAction;
import gxj.study.demo.flink.univ2.model.EventData;
import gxj.study.demo.flink.univ2.model.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:03
 */
@Slf4j
public class TransferOfMint implements State {
    @Override
    public StateEnum accept(EventData eventData, Machine machine) {

        JSONObject event = eventData.getData();
        //空数据
        if (Objects.isNull(event) || Objects.isNull(machine.getCacheEvent())) {
            machine.clearCache();
            return StateEnum.init;
        }

        boolean isMintEvent = StateEnum.mint.name().equals(eventData.getName());
        JSONObject transfer = machine.getCacheEvent().getData();
        if (isNextMint(isMintEvent, event, transfer)) {
            //读取到期望的mint事件，回调mint的doAction方法进行计算。
            StateEnum.mint.getOperator().doAction(machine, eventData);
            return StateEnum.mint;
        } else {
            // 不匹配，说明漏数据，跳过这组action
            log.error("Mint action不匹配异常，transfer:{}, next event(expect mint):{}", JSONObject.toJSONString(transfer), JSONObject.toJSON(event));
            machine.clearCache();
            return StateEnum.init.getOperator().accept(eventData, machine);
        }
    }

    /**
     * 能够匹配 on mint.pairaddress = transfer.pairaddress, mint.txhash = transfer.txhash, mint.logindex = transfer.logindex + 2
     */
    private boolean isNextMint(boolean isMintEvent, JSONObject mint, JSONObject transfer) {
        return isMintEvent &&
                StringUtils.equals(mint.getString("pairAddress"), transfer.getString("pairAddress")) &&
                StringUtils.equals(mint.getString("txHash"), transfer.getString("txHasH")) &&
                Objects.equals(mint.getInteger("logIndex"), transfer.getInteger("logIndex") + 2);
    }

    /**
     * ===== 通用字段 =====
     * chain_id = transfer.chain_id,
     * project = transfer.project,
     * pooladdress = transfer.pairaddress,
     * gas = transfer.gas/(10 ** 18),
     * currenttimestamp = transfer.currenttimestamp,
     * blockno = transfer.blockno,
     * txhash = transfer.txhash,
     * transactionindex = transfer.transactionindex,
     * nonce = transfer.nonce,
     * <p>
     * ===== 特殊字段 =====
     * actiontype = 'mint',
     * owner = transfer.to,
     * liquidity = transfer.value/(10 ** ((decimals0 + decimals1)/2)),
     */
    @Override
    public void doAction(Machine machine, EventData eventData) {
        Pair pair = machine.getPair();
        CalcAction mintAction = new CalcAction(pair);
        //common fields
        mintAction.setCommonFields(eventData);
        //special fields
        JSONObject data = eventData.getData();
        mintAction.setActionType("mint");
        mintAction.setOwner(data.getString("to"));
        double avgDecimals = (pair.getDecimals0() + pair.getDecimals1()) / 2.0;
        mintAction.setLiquidity(data.getBigDecimal("value").divide(BigDecimal.valueOf(Math.pow(10, avgDecimals)), 18, RoundingMode.HALF_UP));

        //set cache
        machine.setCacheAction(mintAction);
    }

}
