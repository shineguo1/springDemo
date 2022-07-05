package gxj.study.demo.flink.univ2.statemachine;

import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.univ2.model.EventData;
import gxj.study.demo.flink.univ2.utils.BlockChainUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:03
 */
public class Init implements State {
    @Override
    public StateEnum accept(EventData eventData, Machine machine) {
        JSONObject data = eventData.getData();
        //空数据
        if (Objects.isNull(data)) {
            return StateEnum.init;
        }
        // mint 动作：transfer、mint
        // burn 动作：transfer、burn
        // transfer 动作
        boolean isTransferEvent = StateEnum.transfer.name().equals(eventData.getName());
        if (isTransferEvent) {
            /* do mint: transfer.from = '0x0000000000000000000000000000000000000000'*/
            if (isMintAction(data)) {
                //匹配 mint action。缓存transfer事件。
                machine.setCacheEvent(eventData);
                //回调transfer(mint)的doAction方法进行计算。
                StateEnum.transferOfMint.getOperator().doAction(machine, eventData);
                return StateEnum.transferOfMint;
            }
            /* do burn： transfer.to = transfer.pairaddress*/
            else if (isBurnAction(data)) {
                //匹配 burn action。缓存transfer事件。
                machine.setCacheEvent(eventData);
                //TODO DO ACTION
                return StateEnum.transferOfBurn;
            }
            /* do transfer：
            * transfer.from != '0x0000000000000000000000000000000000000000' and
            * transfer.to != '0x0000000000000000000000000000000000000000' and
            * transfer.from != transfer.pairaddress and
            * transfer.to != transfer.pairaddress
            */
            else if (isTransferAction(data)) {
                //匹配 transfer action。缓存transfer事件。
                machine.setCacheEvent(eventData);
                //TODO DO ACTION
                return StateEnum.transfer;
            }
        }
        //default;
        return StateEnum.init;
    }

    public static boolean isTransferAction(JSONObject data) {
        return !BlockChainUtils.isAddressEmpty(data.getString("from")) &&
                !StringUtils.equals(data.getString("from"), data.getString("pairAddress")) &&
                !BlockChainUtils.isAddressEmpty(data.getString("to")) &&
                !StringUtils.equals(data.getString("to"), data.getString("pairAddress"));
    }

    public static boolean isMintAction(JSONObject data) {
        return BlockChainUtils.isAddressEmpty(data.getString("from"));
    }

    public static boolean isBurnAction(JSONObject data) {
        return StringUtils.equals(data.getString("to"), data.getString("pairAddress"));
    }

    @Override
    public void doAction(Machine machine, EventData eventData) {

    }
}
