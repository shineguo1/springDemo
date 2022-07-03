package gxj.study.demo.mdc;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xinjie_guo
 * @date 2022/5/16 16:35
 *
 */
public class MdcThreadPool<T extends ThreadPoolExecutor> {

    /**
     * 线程池
     */
    final private T threadPoolExecutor;

    /**
     * 是否使用固定的 MDC上下文
     */
    final private boolean useFixedContext;
    /**
     * 固定的 MDC上下文
     */
    final private Map<String, String> fixedContext;

    public T executor() {
        return threadPoolExecutor;
    }

    public static <T extends ThreadPoolExecutor> MdcThreadPool<T> build(T threadPollExecutor, Map<String, String> fixedContext) {
        return new MdcThreadPool<>(threadPollExecutor, fixedContext);
    }

    private MdcThreadPool(T threadPollExecutor, Map<String, String> fixedContext) {
        this.threadPoolExecutor = threadPollExecutor;
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }

    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    public void execute(Runnable command) {
        threadPoolExecutor.execute(wrap(command, getContextForTask()));
    }

    private static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor threadPollExecutor = new ScheduledThreadPoolExecutor(5);
        MdcThreadPool<ScheduledThreadPoolExecutor> build = MdcThreadPool.build(threadPollExecutor, null);
        //TODO: 需要改成代理模式，不然像下面这种schedulePool独有的方法不能设置MDC
//        build.executor().schedule()
    }
}
