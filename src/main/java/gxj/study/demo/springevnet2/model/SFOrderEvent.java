package gxj.study.demo.springevnet2.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 17:05 0.1.0
 */
@Getter
public class SFOrderEvent extends OrderEvent {

    public SFOrderEvent(OrderDto order) {
        super(order);
    }
}
