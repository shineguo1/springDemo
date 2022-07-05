package gxj.study.demo.flink.univ2.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by xinjie_guo on 2022/7/4.
 */
@AllArgsConstructor
@Getter
public enum StateEnum {

    /** 事件类型即状态机的状态*/

    init("init",new Init()),

    /** 初始状态 */

    transfer("transfer",new Transfer()),
    transferOfMint("transfer",new TransferOfMint()),
    transferOfBurn("transfer",new TransferOfBurn()),
    mint("mint", new Mint()),
    burn("burn", new Burn());

    private String name;
    private State operator;

}
