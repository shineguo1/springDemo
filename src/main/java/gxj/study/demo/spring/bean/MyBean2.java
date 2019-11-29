package gxj.study.demo.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/7 15:35
 * @description
 */
@Component
public class MyBean2 {
    @Autowired
//    @Qualifier("myFactoryBean")
    private MyFactoryBean myBean;
}
