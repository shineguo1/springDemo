package gxj.study.demo.springevnet2;

import gxj.study.demo.springevnet2.model.OrderDto;
import gxj.study.demo.springevnet2.model.OrderEvent;
import gxj.study.demo.springevnet2.model.SFOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 18:08 0.1.0
 */
@Slf4j
@Component
public class OrderListener {


    private VipBiz vipBiz;

    @EventListener(classes = {OrderEvent.class})
    @Order(1)
    public void onEvent1(OrderEvent event) {
        vipBiz.execute(((OrderDto) event.getSource()));
        log.info("[{}] 订单[{}] - 仓库发货", Thread.currentThread().getName(), ((OrderDto) event.getSource()).getOrderNo());
    }


    @EventListener(classes = {OrderEvent.class})
    @Order(2)
    public void onEvent2(OrderEvent event) {
        log.info("[{}] 订单[{}] - 仓库发货", Thread.currentThread().getName(), ((OrderDto) event.getSource()).getOrderNo());
    }

    @EventListener(classes = {SFOrderEvent.class})
    @Order(3)
    public void onEvent3(OrderEvent event) {
        log.info("[{}] 订单[{}] - 仓库发货SF", Thread.currentThread().getName(), ((OrderDto) event.getSource()).getOrderNo());
    }


}
