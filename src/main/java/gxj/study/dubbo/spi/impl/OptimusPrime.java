package gxj.study.dubbo.spi.impl;

import gxj.study.dubbo.spi.Robot;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/13 14:50
 * @description
 */
public class OptimusPrime implements Robot {

    @Override
    public void sayHello() {
        System.out.println("Hello, I am Optimus Prime.");
    }
}