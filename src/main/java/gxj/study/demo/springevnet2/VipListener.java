package gxj.study.demo.springevnet2;

import gxj.study.demo.springevnet2.model.OrderDto;
import gxj.study.demo.springevnet2.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 17:20 0.1.0
 */
//@Component
@Slf4j
public class VipListener {

    public VipListener() {
    }

    @EventListener(condition = "#event.source.vip == true")
    @Async
    @Order(1)
    public void onApplicationEvent(OrderEvent event) {
        log.info("[{}] 订单[{}] - vip客户赠送小礼品", Thread.currentThread().getName(), ((OrderDto) event.getSource()).getOrderNo());
    }

}
