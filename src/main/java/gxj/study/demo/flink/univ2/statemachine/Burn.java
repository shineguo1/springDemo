package gxj.study.demo.flink.univ2.statemachine;

import gxj.study.demo.flink.univ2.model.EventData;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 18:03
 */
public class Burn implements State {
    @Override
    public StateEnum accept(EventData eventData, Machine machine) {
        return StateEnum.init.getOperator().accept(eventData, machine);
    }

    @Override
    public void doAction(Machine machine, EventData eventData) {

    }
}
