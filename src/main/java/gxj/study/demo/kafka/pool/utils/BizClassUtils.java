package gxj.study.demo.kafka.pool.utils;


import gxj.study.demo.kafka.pool.handle.BizHandleInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * <ul>
 * <li>消费者处理控制类</li>
 * <li>控制管理消费者处理类<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public class BizClassUtils {

    /**
     * 处理类map
     */
    public volatile static Map<String, BizHandleInterface> mapObj = new HashMap<>();

    /**
     * 获取相应的处理类
     *
     * @param obj
     * @return
     */
    public static BizHandleInterface get(Object obj) {
        if (null == mapObj.get(obj.getClass().getName())) {
            synchronized (BizClassUtils.class) {
                if (null == mapObj.get(obj.getClass().getName())) {
                    if (obj instanceof BizHandleInterface) {
                        mapObj.put(obj.getClass().getName(), (BizHandleInterface) obj);
                    } else {
                        throw new ClassCastException("illegal changed Exception");
                    }
                }
            }
        }
        return mapObj.get(obj.getClass().getName());
    }
}
