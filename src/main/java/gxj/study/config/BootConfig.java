package gxj.study.config;

import gxj.study.model.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2021/10/20 16:39
 * @description
 */


/*
 * 当proxyBeanMethods = true，BootConfig这个bean就是CGLib代理的，于是bootConfig.userBean1()得到的是容器里的单例bean。
 * 当proxyBeanMethods = false，BootConfig这个bean就是普通实例，于是bootConfig.userBean1()是普通的方法调用，得到的是普通实例。
 */
@Configuration(proxyBeanMethods = false)
public class BootConfig {

    @Bean
    public UserDTO userBean1(){
        return new UserDTO();
    }

}
