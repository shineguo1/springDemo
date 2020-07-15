package gxj.study.dubbo.spi.impl;

import gxj.study.dubbo.spi.AOPInterface;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 13:44
 * @description
 */
public class AnotherWrapper implements AOPInterface{
    private AOPInterface instance;
    public AnotherWrapper(AOPInterface instance){
        this.instance= instance;
    }

    @Override
    public void sayHello() {
        System.out.println("Another Wrapper: call sayHello begin");
        instance.sayHello();
        System.out.println("Another Wrapper: call sayHello end");
    }
}
