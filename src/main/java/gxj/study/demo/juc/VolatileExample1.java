package gxj.study.demo.juc;

import gxj.study.demo.requestMerge.User;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/14 9:44
 * @description 多线程对共享变量的可见性
 */
public class VolatileExample1 extends Thread {

    //设置类静态变量,各线程访问这同一共享变量
    private static boolean flag = false;

    //无限循环,等待flag变为true时才跳出循环
    @Override
    public void run() {
        //如果进debug模式，一打断点就会退出循环；run模式会死循环。猜测：线程进入断点时，工作内存中的变量会刷新为主内存中的数值。

        /*
        * 1. example线程 初始化时将flag = false 写入线程栈区，于是线程会陷入while死循环。
        * 2. 当main线程将内存中的flag写为true时，example线程的栈区中的flag=false不变，依旧陷入while死循环。
        * 3. 当flag加上关键字volatile时，example线程每次都会直接从主内存中读写flag的值，所以有能力跳出循环。
        * 4. 当example线程中执行synchronized(this)锁时，会从主内存刷新所有共享变量到工作内存，所以线程栈区中的flag被写为true，也有能力跳出循环。
        *    相关知识：JVM对synchronized的规定：
        *       1、线程解锁前，必须把共享变量的最新值刷新到主内存中；
        *       2、线程加锁时，讲清空工作内存中共享变量的值，从而使用共享变量是需要从主内存中重新读取最新的值
        */
        while (!flag) {
            /*
            * 现象：打开print会退出循环，注释print会陷入死循环。
            * 原因：print和println方法都会对this上synchronized锁
            */
//           System.out.println("hello world");
        }
    }

    public static void main(String[] args) throws Exception {
        new VolatileExample1().start();
        //sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
        Thread.sleep(100);
        flag = true;
        System.out.println("flag = true");
    }
}