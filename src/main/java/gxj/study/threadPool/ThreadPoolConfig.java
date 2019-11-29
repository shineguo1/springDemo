package gxj.study.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/29 9:23
 * @description
 */
public class ThreadPoolConfig {
    int corePoolSize = 3;
    int maximumPoolSize = 5;
    long keepAliveTime = 3000;
    TimeUnit miliseconds = TimeUnit.MILLISECONDS;
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);

}
