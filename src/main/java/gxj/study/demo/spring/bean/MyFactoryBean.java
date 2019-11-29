package gxj.study.demo.spring.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/6 17:55
 * @description
 */
@Component
public class MyFactoryBean implements FactoryBean{

    public MyFactoryBean(){
        System.out.println("MyFactoryBean构造方法");
    }

    @Override
    public Object getObject() throws Exception {
        return new MyBean();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}


