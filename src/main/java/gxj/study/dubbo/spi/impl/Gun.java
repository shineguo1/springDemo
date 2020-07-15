package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.extension.Adaptive;
import gxj.study.dubbo.spi.Weapon;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/13 17:17
 * @description
 */
public class Gun implements Weapon {
    @Override
    @Adaptive
    public void sayHello() {
        System.out.println("Hello, I am Gun");
    }

}
