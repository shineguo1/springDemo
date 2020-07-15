package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import gxj.study.dubbo.spi.WheelMaker;
import gxj.study.dubbo.spi.model.Wheel;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 11:34
 * @description
 */
public class MichelinWheelMaker implements WheelMaker {

    @Override
    public Wheel makeWheel(URL url) {
        System.out.println("米其林轮胎");
        return new Wheel();
    }
}
