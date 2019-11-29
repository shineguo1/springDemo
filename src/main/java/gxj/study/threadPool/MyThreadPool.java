package gxj.study.threadPool;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/29 9:21
 * @description
 */
public class MyThreadPool {

//    ThreadPoolExecutor pool;

    private static int corePoolSize = 3;
    private static int maximumPoolSize = 5;
    private static long keepAliveTime = 3000;
    private static TimeUnit milliseconds = TimeUnit.MILLISECONDS;
    private static RejectedExecutionHandler reject = new RejectedExecutionHandler() {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Runnable " + r + " 被拒绝");
        }
    };

    private static ThreadPoolExecutor poolExecutor;

    /**
     * 默认无界任务队列
     */
    MyThreadPool() {
        initPoolExecutor();
    }

    private static void initPoolExecutor() {
        poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                milliseconds, new LinkedBlockingDeque<>(), reject);
    }

    /**
     * @param taskSize 任务队列大小
     */
    private static void initPoolExecutor(int taskSize) {
        poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                milliseconds, new LinkedBlockingDeque<>(taskSize), reject);
    }


    public static ExecutorService getThreadPool() {
        if (poolExecutor == null) {
            synchronized (MyThreadPool.class) {
                if (poolExecutor == null) {
                    initPoolExecutor(5);
                }
            }
        }
        return poolExecutor;
    }

}

class Test {

    public static void main(String[] args) throws InterruptedException {
        // 核心线程3；最大线程5；任务队列5。最多可容纳10个任务，可同时执行5个任务，可保留3个空闲线程。
        ExecutorService threadPool = MyThreadPool.getThreadPool();
        /*
        * 说明：
        * 0,1,2是核心线程先执行，第一批打印
        * 3、4、5、6、7进入任务队列，最后执行，最后打印
        * 8、9由于任务队列已满，另开线程执行（不超过最大线程），第二批打印
        * 10、11、12、13由于任务队列和最大线程数都满了，被拒绝执行
         */
        for (int i = 0; i < 14; i++) {
            int n = i;
            //稍微休眠一下，让打印能分出先后
            Thread.sleep(10);
            Runnable r = () -> {
                try {
                    System.out.println("执行任务 " + n);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            };
            threadPool.submit(r);
        }
    }
}
