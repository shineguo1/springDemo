package gxj.study.dubbo.spi.impl;

import gxj.study.dubbo.spi.AOPInterface;
import gxj.study.dubbo.spi.Robot;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 13:44
 * @description
 */
public class AOPWrapper implements AOPInterface{
    private AOPInterface instance;
    public AOPWrapper(AOPInterface instance){
        this.instance= instance;
    }

    @Override
    public void sayHello() {
        System.out.println("AOP wrapper: call sayHello begin");
        instance.sayHello();
        System.out.println("AOP wrapper: call sayHello end");
    }
}
