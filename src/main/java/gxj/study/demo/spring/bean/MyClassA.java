package gxj.study.demo.spring.bean;

import gxj.study.util.SpringContextHolder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/27 11:15
 * @description
 */
@Component
@Data
public class MyClassA {

    @Autowired
    private MyBean3 bean3;

    public void test(){

        System.out.println(getBean3().getName());
        ApplicationContext ac = SpringContextHolder.getApplicationContext();
        MyBean3 myBean3 = (MyBean3) ac.getBean("myBean3");
        myBean3.setName("123");
        System.out.println(getBean3().getName());

    }
}
