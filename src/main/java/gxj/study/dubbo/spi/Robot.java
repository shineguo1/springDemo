package gxj.study.dubbo.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/13 14:49
 */
@SPI
public interface Robot {
    void sayHello();
}
