package gxj.study.demo.okhttp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import okhttp3.Dispatcher;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2023/3/6 16:44
 */
public class MyDispatcherFactory {

    private static ExecutorService myOkHttpThreadPool() {
        return new ThreadPoolExecutor(10, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
                new ThreadFactoryBuilder().setNameFormat("Mini OkHttp ThreadPool ForTest-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static Dispatcher create() {
        Dispatcher dispatcher = new Dispatcher(myOkHttpThreadPool());
        //设置perHost并发请求数与总并发请求数相等，即解除对host的限制。
        dispatcher.setMaxRequests(64);
        dispatcher.setMaxRequestsPerHost(64);
        return dispatcher;
    }

}
