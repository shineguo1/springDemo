package gxj.study.demo.springevent;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/29 13:48
 * @description
 */
@Component
public class MyListenerAll implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("MyListenerAll: 监听所有事件 | event:" + event);
    }
}
