package gxj.study.juc;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/14 17:45
 * @description
 */
public class SynchronizedDemo1 implements Runnable {
    public synchronized void get() {
        System.out.println("2 enter thread name-->" + Thread.currentThread().getName());
        //reentrantLock.lock();
        System.out.println("3 get thread name-->" + Thread.currentThread().getName());
        set();
        //reentrantLock.unlock();
        System.out.println("5 leave run thread name-->" + Thread.currentThread().getName());
    }

    public synchronized void set() {
        //reentrantLock.lock();
        System.out.println("4 set thread name-->" + Thread.currentThread().getName());
        //reentrantLock.unlock();
    }

    @Override
    public void run() {
        System.out.println("1 run thread name-->" + Thread.currentThread().getName());
        get();
    }

    public static void main(String[] args) {
        SynchronizedDemo1 test = new SynchronizedDemo1();
        for (int i = 0; i < 100; i++) {
            new Thread(test, "thread-" + i).start();
        }
    }


}
