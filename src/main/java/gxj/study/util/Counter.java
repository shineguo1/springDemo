package gxj.study.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 16:18
 * @description
 */
public class Counter {

    private static AtomicInteger count = new AtomicInteger(0);

    public static int getCount(){
        return count.incrementAndGet();
    }

}
