package gxj.study.demo.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/15 9:30
 * @description 测试lock、tryLock和lockInterruptibly
 */
public class ReentrantLockDemo2 {

    private ReentrantLock lock = new ReentrantLock();

    private Thread getLockThread(int sleepTime) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "run");
                lock.lock();
                doTask(lock, sleepTime);
            }
        });
    }


    private Thread getTryLockThread(int sleepTime, int timeout) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "run");
                try {
                    if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                        doTask(lock, sleepTime);
                    } else {
                        System.out.println(Thread.currentThread() + "超时放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Thread getLockInterruptibly() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "run");
                try {
                    lock.lockInterruptibly();
                    doTask(lock,5000);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread() + e.getClass().toString());
                }
                System.out.println(Thread.currentThread() + "do私有工作");
            }
        });
    }

    private void doTask(ReentrantLock lock, int sleepTime) {
        try {
            System.out.println(Thread.currentThread() + "得到了锁.");
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.unlock();
                System.out.println(Thread.currentThread() + "释放了锁.");
            }
            catch (Exception e){
                System.out.println(Thread.currentThread() + "释放锁失败");
            }
        }
    }


    private void testLock() {
        Thread t1 = getLockThread(1000);
        Thread t2 = getLockThread(1000);
        t1.start();
        t2.start();

    }

    private void testTryLockThread(boolean testTimeout) {
        int sleepTime = 1000;
        int timeout = testTimeout ? 1500 : 100;
        Thread t1 = getTryLockThread(sleepTime, timeout);
        Thread t2 = getTryLockThread(sleepTime, timeout);
        t1.start();
        t2.start();
    }

    private void testLockInterruptibly(boolean isInterrupt) throws InterruptedException {
        Thread t1 = getLockInterruptibly();
        Thread t2 = getLockInterruptibly();
        String name = t2.getName();
        t1.start();
        t2.start();
        if(isInterrupt) {
            Thread.sleep(1000);
            System.out.println(name + " 中断指令");
            t2.interrupt();
        }
//        t2.start();
    }

    public static void main(String[] args) throws InterruptedException {
          ReentrantLockDemo2 demo = new ReentrantLockDemo2();

        System.out.println("/*\n* 测试lock()方法\n*/");
        demo.testLock();
        Thread.sleep(3500);

        System.out.println("=====");
        System.out.println("/*\n* 测试tryLock()方法 - 超时\n*/");
        demo.testTryLockThread(false);
        Thread.sleep(3500);

        System.out.println("=====");
        System.out.println("/*\n* 测试tryLock()方法 - 未超时\n*/");
        demo.testTryLockThread(true);
        Thread.sleep(3500);

        System.out.println("=====");
        System.out.println("/*\n* 测试lockInterruptibly()方法 - 中断\n*/");
        demo.testLockInterruptibly(true);
        Thread.sleep(6000);

        System.out.println("=====");
        System.out.println("/*\n* 测试lockInterruptibly()方法 - 不中断\n*/");
        demo.testLockInterruptibly(false);
    }

}
