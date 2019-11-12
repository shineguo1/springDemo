//package gxj.study.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author xinjie_guo
// * @version 1.0.0 createTime:  2019/10/30 13:33
// * @description
// */
//@Component("a")
//public class AService {
//    @Autowired
//    BService b;
//
//    AService() {
//        System.out.println("A 构造函数");
//    }
//
//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("A PostConstruct");
//        Son son = new Son();
//        son.publicMethod();
//        son.protectedMethod();
//        son.defaultMethod();
//        son.finalMethod();
//    }
//
//    public void publicMethod() {
//
//    }
//
//    protected void protectedMethod() {
//
//    }
//
//    void defaultMethod() {
//
//    }
//
//    private void privateMethod() {
//
//    }
//
//    final void finalMethod() {
//
//    }
//
//    static void staticMethod() {
//
//    }
//}
//
//class Son extends AService{
//
//    public static void staticMethod (){
//
//    }
//
//}
