package gxj.study.demo.springevnet2;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 17:21 0.1.0
 */
@EnableAsync(proxyTargetClass = false)  // 关键：使用JDK代理
@Configuration
public class EnvConfig {
}
