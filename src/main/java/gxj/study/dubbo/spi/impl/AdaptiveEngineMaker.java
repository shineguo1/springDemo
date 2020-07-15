package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import gxj.study.dubbo.spi.EngineMaker;
import gxj.study.dubbo.spi.WheelMaker;
import gxj.study.dubbo.spi.model.Engine;
import gxj.study.dubbo.spi.model.Wheel;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 9:37
 * @description
 */
@Adaptive
public class AdaptiveEngineMaker implements EngineMaker {
    @Override
    public Engine makeEngine(URL url) {
        System.out.println("AdaptiveEngineMaker:choose instance");
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }

        // 1.从 URL 中获取 WheelMaker 名称
        String engineMakerName = url.getParameter("engineType");
        if (engineMakerName == null) {
            throw new IllegalArgumentException("engineMakerName == null");
        }

        // 2.通过 SPI 加载具体的 WheelMaker
        EngineMaker engineMaker = ExtensionLoader
                .getExtensionLoader(EngineMaker.class).getExtension(engineMakerName);

        // 3.调用目标方法
        return engineMaker.makeEngine(url);
    }
}