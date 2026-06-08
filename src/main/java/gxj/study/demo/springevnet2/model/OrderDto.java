package gxj.study.demo.springevnet2.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author xinjie_guo
 * @version 0.1.0
 * @since 2025/12/25 17:05 0.1.0
 */
@Getter
public class OrderDto {

    private static final AtomicInteger atomicInteger = new AtomicInteger(1000);

    private final Integer orderNo;
    private final String goodsName;
    private final boolean vip;

    public OrderDto(String goodsName, boolean vip) {
        this.orderNo = atomicInteger.getAndIncrement();
        this.goodsName = goodsName;
        this.vip = vip;
    }
}
