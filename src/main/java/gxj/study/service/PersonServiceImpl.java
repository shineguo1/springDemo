package gxj.study.service;

import gxj.study.util.SpringContextHolder;
//import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/4 16:36
 * @description
 */
@Component("proxyService")
//@Mapper
public class PersonServiceImpl implements PersonInterface {
//    @Autowired
//    MyFactoryBean myBean;
    public PersonServiceImpl(){
        System.out.println("PersonServiceImpl构造方法");
    }

    @Override
    public String getName() {
        System.out.println("override pikachu");
        return "pikachu";
    }

    public String getName2() {
        System.out.println("public pikachu");
        return "pikachu2";
    }

    public void call(){

        ApplicationContext ac = SpringContextHolder.getApplicationContext();
        PersonServiceImpl p = (PersonServiceImpl) ac.getBean("proxyService");
        p.getName();
        p.getName2();
        p.getName3();
        PersonServiceImpl.getName4();
        p.getName5();
        p.getName6();
        p.getName7();
        System.out.println("断点:"+p.getClass().toGenericString());
    }

    protected String getName3() {
        System.out.println("protected pikachu");
        return "pikachu2";
    }

    private String getName6() {
        System.out.println("private pikachu");
        return "pikachu2";
    }

     String getName7() {
        System.out.println("default pikachu");
        return "pikachu2";
    }

    static String getName4() {
        System.out.println("static pikachu");
        return "pikachu2";
    }

    final String getName5() {
        System.out.println("final pikachu");
        return "pikachu2";
    }

    public static void main(String[] args) {
        ApplicationContext ac = SpringContextHolder.getApplicationContext();
//        PersonInterface p = (PersonInterface) ac.getBean("proxyService");
        PersonServiceImpl p = (PersonServiceImpl) ac.getBean("proxyService");
//        PersonServiceImpl p = new PersonServiceImpl();
//        PersonInterface proxy = (PersonInterface) Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(), p.getClass().getInterfaces(), invocation);
//        proxy.getName();
        p.getName();
        p.getName2();
        p.getName3();
        System.out.println("断点:"+p.getClass().toGenericString());
    }
}
