package gxj.study.dubbo.spi.bean;

import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/15 9:22
 * @description
 */
@Component
public class AnotherGlassMaker extends GlassMaker{

    @Override
    public void makeGlass(){
        System.out.println("SpringBean copy玻璃");
    }
}
