package gxj.study.demo.flink.univ2.statemachine;

import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.univ2.model.EventData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:03
 */
@Slf4j
public class TransferOfBurn implements State {
    @Override
    public StateEnum accept(EventData eventData, Machine machine) {

        JSONObject event = eventData.getData();
        //空数据
        if (Objects.isNull(event) || Objects.isNull(machine.getCacheEvent())) {
            machine.clearCache();
            return StateEnum.init;
        }

        //匹配 on burn.pairaddress = transfer.pairaddress, burn.txhash = transfer.txhash, burn.logindex = transfer.logindex + min(k)
        boolean isBurnEvent = StateEnum.burn.name().equals(eventData.getName());
        boolean isTransferEvent = StateEnum.transfer.name().equals(eventData.getName());
        JSONObject transfer = machine.getCacheEvent().getData();
        //读取到期望的burn事件
        if (isNextBurn(isBurnEvent, event, transfer)) {
            //TODO DO ACTION
            return StateEnum.burn;
        }
        //读到action的开始标志事件（符合条件的transfer事件）
        else if (isTransferEvent && (Init.isBurnAction(event) || Init.isMintAction(event) || Init.isTransferAction(event))) {
            return StateEnum.init.getOperator().accept(eventData, machine);
        }
        //未找到burn，状态不变，continue
        else {
            return StateEnum.transferOfBurn;
        }
    }

    private boolean isNextBurn(boolean isBurnEvent, JSONObject burn, JSONObject transfer) {
        return isBurnEvent &&
                StringUtils.equals(burn.getString("pairAddress"), transfer.getString("pairAddress")) &&
                StringUtils.equals(burn.getString("txHash"), transfer.getString("txHasH")) &&
                burn.getInteger("logIndex") > transfer.getInteger("logIndex");
    }

    @Override
    public void doAction(Machine machine, EventData eventData) {
        return null;
    }
}
