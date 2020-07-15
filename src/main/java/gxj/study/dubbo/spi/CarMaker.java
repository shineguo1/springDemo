package gxj.study.dubbo.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.SPI;
import gxj.study.dubbo.spi.model.Car;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 9:39
 */
@SPI
public interface CarMaker {
    Car makeCar(URL url);
}