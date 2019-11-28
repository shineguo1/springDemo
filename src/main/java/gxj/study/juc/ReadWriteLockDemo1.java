package gxj.study.juc;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/15 14:46
 * @description
 */
public class ReadWriteLockDemo1 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    private Thread getThread(Lock lock, String threadName, int sleep) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println(new Date().toString() + " " + Thread.currentThread() + " 加锁 " + lock.getClass().getSimpleName());
                    Thread.sleep(sleep);
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                    System.out.println(new Date().toString() + " " + Thread.currentThread() + " 解锁 " + lock.getClass().getSimpleName());
                }
            }
        }, threadName);
    }

    private void test() throws InterruptedException {
        getThread(writeLock, "thread-4", 2000).start();
        getThread(readLock, "thread-1", 3000).start();
        getThread(readLock, "thread-2", 1000).start();
        getThread(readLock, "thread-3", 4000).start();
        getThread(writeLock, "thread-5", 1000).start();
        // 让写锁先加入等待队列
        Thread.sleep(1100);
        getThread(readLock, "thread-6", 1000).start();
        Thread.sleep(10);
        getThread(writeLock, "thread-7", 1000).start();
    }

    public static void main(String[] args) throws InterruptedException {
        new ReadWriteLockDemo1().test();
    }
}
