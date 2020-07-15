package gxj.study.dubbo.spi.impl;

import gxj.study.dubbo.spi.AOPInterface;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 13:44
 * @description
 */
public class AOPImpl implements AOPInterface{

    @Override
    public void sayHello() {
        System.out.println("Hello, I am AOP implement class.");
    }
}
