package gxj.study.demo.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;

/**
 * @author xinjie_guo
 * @date 2022/5/16 11:20
 */
public class GuavaRetryHelper {
    public static <T> Retryer<T> defaultRetry() {
        return RetryerBuilder.<T>newBuilder()
                .retryIfRuntimeException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                .build();
    }
}
