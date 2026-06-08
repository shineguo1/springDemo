package gxj.study.demo.springevnet2;

import gxj.study.demo.springevnet2.model.OrderDto;
import gxj.study.demo.springevnet2.model.OrderEvent;
import gxj.study.demo.springevnet2.model.SFOrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 16:59 0.1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public void order(OrderDto order) {
        log.info("[{}] 收到订单[{}]", Thread.currentThread().getName(), order.getOrderNo());
        eventPublisher.publishEvent(new OrderEvent(order));
        log.info("[{}] 下单结束！！！", Thread.currentThread().getName());
    }


    public void orderSF(OrderDto order) {
        log.info("[{}] 收到SF订单[{}]", Thread.currentThread().getName(), order.getOrderNo());
        eventPublisher.publishEvent(new SFOrderEvent(order));
        log.info("[{}] SF下单结束！！！", Thread.currentThread().getName());
    }
}
