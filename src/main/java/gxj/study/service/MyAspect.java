package gxj.study.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/4 9:09
 * @description
 */
@Aspect
//@Component
@EnableAspectJAutoProxy(proxyTargetClass = false)
class MyAspect {

    public MyAspect(){
        System.out.println("MyAspect构造方法");
    }
//    @Pointcut("execution(* fun*(..))")
//    public void pointCut(){
//        System.out.println("this is pointcut");
//    }

//    @Pointcut("execution(* getNam*(..))")
//    public void pointCut(){
//        System.out.println("this is pointcut");
//    }

//    @After("pointCut()")
//    public void after(JoinPoint joinPoint) {
//        System.out.println("after pointcut");
//    }
//
//    @Before("pointCut()")
//    public void before(JoinPoint joinPoint) {
//        //如果需要这里可以取出参数进行处理
//        //Object[] args = joinPoint.getArgs();
//        System.out.println("before pointcut");
//    }

//    @AfterReturning(
//            pointcut="pointCut()",
//            returning="retVal")
//    public void doAccessCheck(Object retVal) {
//        System.out.println("afterReturn pointcut, return result is "
//                + retVal);
//    }

    @Around("this(gxj.study.service.PersonServiceImpl)")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("===around start====");
        pjp.proceed();
        System.out.println("===around end===");
    }

//    @AfterThrowing(pointcut = "pointCut()", throwing = "error")
//    public void afterThrowing(JoinPoint jp, Throwable error) {
//        System.out.println("error:" + error);
//    }

    public static void main(String[] args) {

    }

}
