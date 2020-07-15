package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import gxj.study.dubbo.spi.EngineMaker;
import gxj.study.dubbo.spi.WheelMaker;
import gxj.study.dubbo.spi.model.Engine;
import gxj.study.dubbo.spi.model.Wheel;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 11:34
 * @description
 */
public class FerrariEngineMaker implements EngineMaker {

    @Override
    public Engine makeEngine(URL url) {
        System.out.println("法拉利引擎");
        return new Engine();
    }
}
