package gxj.study.demo.springevnet2;

import gxj.study.demo.springevnet2.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 18:09 0.1.0
 */
@Slf4j
@Component
public class VipBiz implements BaseBiz {

    @Async
    public void execute(OrderDto dto) {
        log.info("[{}] 订单[{}] - vip客户赠送小礼品", Thread.currentThread().getName(), dto.getOrderNo());
    }
}
