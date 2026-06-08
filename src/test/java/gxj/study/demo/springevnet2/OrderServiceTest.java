package gxj.study.demo.springevnet2;

import gxj.study.demo.springevnet2.model.OrderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 17:29 0.1.0
 */
@ComponentScan(basePackages = {
        "gxj.study.demo.springevnet2"
})
@EnableAsync(proxyTargetClass = false)  // 关键：使用JDK代理
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceTest.class)
public class OrderServiceTest  {
    @Autowired
    OrderService orderService;

    @Test
    public void test() {
        orderService.order(new OrderDto("咖啡", false));
        orderService.order(new OrderDto("海鲜", true));
    }

}
