package gxj.study.demo.retry;

import com.github.rholder.retry.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @author xinjie_guo
 * @date 2022/5/15 18:16
 */
@Service
@Slf4j
public class SpringRetryService {

    private ThreadLocal<Integer> count = new ThreadLocal<>();

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(value = 0L))
    public String doRequest(String param1, String param2) {
        //线程自增，记录运行次数
        if (count.get() == null) {
            count.set(1);
        } else {
            count.set(count.get() + 1);
        }
        log.info(Thread.currentThread().getName() + " 调用doRequest ... 当前count " + count.get());
        if (count.get() >= 5) {
            return "success";
        } else {
            throw new CannotGetJdbcConnectionException("未达到重试次数，继续抛出异常");
        }
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(value = 0L))
    public void doRequestVoid() {
        //线程自增，记录运行次数
        if (count.get() == null) {
            count.set(1);
        } else {
            count.set(count.get() + 1);
        }
        log.info(Thread.currentThread().getName() + " 调用doRequest ... 当前count " + count.get());
        if (count.get() >= 5) {
            return;
        } else {
            throw new CannotGetJdbcConnectionException("未达到重试次数，继续抛出异常");
        }
    }

    /**
     * 参数个数和类型完全匹配才会被recover捕获.即只能捕获 T method(String,String) 格式的方法.
     * 所以这个recover只能捕获doRequest的重试全部失败后的异常,不能捕获doRequestVoid
     *
     */
    @Recover
    private Object recover(Exception e, String param1, String param2) {
        count.remove();
        log.error("recover catch error. param1:{}, param2:{}, error type:{}, e:{}", param1, param2, e.getClass().getSimpleName(), e.getMessage());
        return null;
    }


    public static void main(String[] args) throws ExecutionException, RetryException {
        log.info("===guava retry===");
        SpringRetryService bizService = new SpringRetryService();
        GuavaRetryHelper.<Void>defaultRetry().call(() -> {
            bizService.doRequestVoid();
            return null;
        });
    }
}
