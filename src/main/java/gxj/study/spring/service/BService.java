//package gxj.study.service;
//
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
//@Component("b")
//public class BService {
//    @Autowired
//    AService a;
//
//    BService() {
//        System.out.println("B 构造函数");
//    }
//
//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("B PostConstruct");
//    }
//
//    public String func1(int i) {
//        String ret = "myResult";
//        System.out.println("in function" + ret);
//        return ret;
//    }
//
//    public void func2() throws Exception {
//        System.out.println("in function2");
//        System.out.println("throw exception");
//        if (1 == 1) {
//            throw new Exception("123");
//        }
//    }
//}
