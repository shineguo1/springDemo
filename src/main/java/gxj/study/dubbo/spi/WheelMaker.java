package gxj.study.dubbo.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import gxj.study.dubbo.spi.model.Wheel;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 9:35
 * @description
 */
@SPI
public interface WheelMaker {
    @Adaptive(value = "wheelType")
    Wheel makeWheel(URL url);
}
