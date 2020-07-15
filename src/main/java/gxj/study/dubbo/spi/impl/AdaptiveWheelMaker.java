package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import gxj.study.dubbo.spi.WheelMaker;
import gxj.study.dubbo.spi.model.Wheel;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 9:37
 * @description
 */
public class AdaptiveWheelMaker implements WheelMaker {
    @Override
    public Wheel makeWheel(URL url) {
        System.out.println("AdaptiveWheelMaker:choose instance");
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }

        // 1.从 URL 中获取 WheelMaker 名称
//        String wheelMakerName = url.getParameter("Wheel.maker");
        String wheelMakerName = url.getParameter("wheelType");
        if (wheelMakerName == null) {
            throw new IllegalArgumentException("wheelMakerName == null");
        }

        // 2.通过 SPI 加载具体的 WheelMaker
        WheelMaker wheelMaker = ExtensionLoader
                .getExtensionLoader(WheelMaker.class).getExtension(wheelMakerName);

        // 3.调用目标方法
        return wheelMaker.makeWheel(url);
    }
}