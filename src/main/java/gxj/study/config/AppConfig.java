package gxj.study.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
