package gxj.study.config;

import gxj.study.service.MyImportBeanDefinitionRegistrar;
import gxj.study.service.MyImportSelector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/5 10:55
 * @description
 */
@Configuration
@ComponentScan("gxj.study")
//@Import({MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})
//@Import({MyImportBeanDefinitionRegistrar.class})
//@Import({MyImportSelector.class})

//@EnableAspectJAutoProxy
public class AppConfig {
    public AppConfig(){
        System.out.println("AppConfig构造方法");
    }
}
