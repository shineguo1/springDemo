package gxj.study.demo.flink.univ2.statemachine;

import gxj.study.demo.flink.univ2.model.EventData;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/4 17:55
 */
public interface State {

    StateEnum accept(EventData eventData, Machine machine);

    void doAction(Machine machine, EventData eventData);
}
