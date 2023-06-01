package gxj.study.demo.okhttp;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2023/3/6 16:45
 */
public class Main {

    /**
     * 1.`OkHttpClient`通过`okhttp3.OkHttpClient.Builder`创建，builder中可以配置连接时间、超时时间、分发器（dispatcher）
     * 2. dispatcher中可以配置线程池策略和连接池策略。
     * 3. builder默认的dispatcher是在`Builder无参构造函数`里，通过`new Dispatcher()`构建的，可以通过set函数覆盖。
     * 4. Dispatcher默认的线程池是在`executorService()`方法里懒加载(lazy)构建的。可以在构造函数`Dispatcher(ExecutorService)`显式自定义。
     * 5. Dispatcher默认的连接池参数是在`Dispatcher()`无参构造函数里定义的，其他构造函数会调用这个无参构造函数初始化连接池参数。可以通过set方法自定义。
     * 4. dispatcher 默认的线程池如下，即阻塞队列, 但有多少任务开多少线程（最大MAX_INTEGER）。
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, (BlockingQueue)(new SynchronousQueue()), Util.threadFactory("OkHttp Dispatcher", false));
     * 5. dispatcher 默认的连接池参数如下，相同host默认最大并发数是5(maxRequestsPerHost), 如果请求的host是特定的（只有个别几个）, 建议调整这个参数。，
     * ```
     *     public Dispatcher() {
     *         this.maxRequests = 64;
     *         this.maxRequestsPerHost = 5;
     *         this.readyAsyncCalls = new ArrayDeque();
     *         this.runningAsyncCalls = new ArrayDeque();
     *         this.runningSyncCalls = new ArrayDeque();
     *     }
     * ```
     */
}
