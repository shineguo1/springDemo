package gxj.study.demo.springevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/29 13:45
 * @description
 */
@Service
public class MyService {
    @Autowired
    ApplicationContext applicationContext;

    public void doSomething() {
        System.out.println(Thread.currentThread().getName() + ": do something 第一步");
        //发布MyEvent事件
        System.out.println(Thread.currentThread().getName() + ": 发布MyEvent事件");
        applicationContext.publishEvent(new MyEvent(applicationContext));

        //发布MyEvent2事件
        System.out.println(Thread.currentThread().getName() + ": 发布MyEvent2事件");
        applicationContext.publishEvent(new MyEvent2(applicationContext));

        System.out.println(Thread.currentThread().getName() + ": 工作流完成！！！");
    }
}
