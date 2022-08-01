package gxj.study.demo.retry;

//import com.github.rholder.retry.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinjie_guo
 * @date 2022/5/15 18:16
 */
@Service
@Slf4j
public class AtomicRetryService {

    private AtomicInteger count = new  AtomicInteger(0);

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(value = 0L))
    public String doRequest(String param1, String param2) {
        //线程自增，记录运行次数
        if (count.get() == 0) {
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
        if (count.get() == 0) {
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

}
