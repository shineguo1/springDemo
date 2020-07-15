package gxj.study.dubbo.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/13 17:16
 */
@SPI
public interface Weapon {
    void sayHello();
}
