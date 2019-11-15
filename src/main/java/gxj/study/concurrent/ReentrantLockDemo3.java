package gxj.study.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/15 11:20
 * @description 是否可以一个线程加锁另一个线程解锁 - 不能
 */
public class ReentrantLockDemo3 {


    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("t1加锁");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                lock.unlock();
                System.out.println("t1解锁");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
//                lock.unlock();
//                System.out.println("t2解锁");

                lock.lock();
                System.out.println("t2加锁");
                lock.unlock();
                System.out.println("t2解锁");
            }
        });

        t1.start();
        t2.start();
    }

}
