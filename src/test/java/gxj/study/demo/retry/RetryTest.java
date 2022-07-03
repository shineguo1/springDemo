package gxj.study.demo.retry;

import com.github.rholder.retry.RetryException;
import gxj.study.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

@Slf4j
public class RetryTest extends BaseTest {

    @Autowired
    private SpringRetryService bizService;

    @Test
    public void spring_retry() {
        log.info("===spring retry===");
        try {
            String s = bizService.doRequest("a", "b");
            System.out.println("result:" + s);
        } catch (Exception e) {
            //重试抛出的异常会被recover捕获，这里捕获的是recover抛出的异常
            System.out.println("catch Exception, send warning: " + e.getMessage());
        }
    }

    @Test
    public void spring_retry_void() {
        log.info("===spring retry===");
        try {
            bizService.doRequestVoid();
            System.out.println("result void");
        } catch (Exception e) {
            //重试抛出的异常会被recover捕获，这里捕获的是recover抛出的异常
            System.out.println("catch Exception, send warning: " + e.getMessage());
        }
    }

    @Test
    public void guava_retry() throws ExecutionException, RetryException {
        log.info("===guava retry===");
        SpringRetryService bizService = new SpringRetryService();
        String call = GuavaRetryHelper.<String>defaultRetry().call(() -> bizService.doRequest("", ""));
        System.out.println("result:" + call);
    }

    @Test
    public void guava_retry_void() throws ExecutionException, RetryException {
        log.info("===guava retry===");
        SpringRetryService bizService = new SpringRetryService();
        GuavaRetryHelper.<Void>defaultRetry().call(() -> {
            bizService.doRequestVoid();
            return null;
        });
        System.out.println("result void");
    }
}