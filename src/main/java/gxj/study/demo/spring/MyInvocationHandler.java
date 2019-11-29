//package gxj.study.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//
///**
// * @author xinjie_guo
// * @version 1.0.0 createTime:  2019/11/4 17:01
// * @description
// */
//@Component
//public class MyInvocationHandler implements InvocationHandler {
//    @Autowired
//    PersonServiceImpl o;
//
//    public MyInvocationHandler(PersonServiceImpl o){
//        this.o=o;
//    }
//
//    public MyInvocationHandler(){
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("==before InvocationHandler invoke==");
//        Object result = method.invoke(o);
//        System.out.println("==after InvocationHandler invoke==");
//        return result;
//    }
//}
