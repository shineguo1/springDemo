package gxj.study.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/14 17:48
 * @description
 */
public class ReentrantLockDemo1 implements Runnable {

    private ReentrantLock reentrantLock = new ReentrantLock();

    public void get() {
        System.out.println("2 enter thread name-->" + Thread.currentThread().getName());
        reentrantLock.lock();
        System.out.println("3 get thread name-->" + Thread.currentThread().getName());
        set();
        reentrantLock.unlock();
        System.out.println("5 leave run thread name-->" + Thread.currentThread().getName());
    }

    public void set() {
        reentrantLock.lock();
        System.out.println("4 set thread name-->" + Thread.currentThread().getName());
        reentrantLock.unlock();
    }

    @Override
    public void run() {
        System.out.println("1 run thread name-->" + Thread.currentThread().getName());
        get();
    }

    public static void main(String[] args) {
        ReentrantLockDemo1 test = new ReentrantLockDemo1();
        for (int i = 0; i < 10; i++) {
            new Thread(test, "thread-" + i).start();
        }
    }

}

