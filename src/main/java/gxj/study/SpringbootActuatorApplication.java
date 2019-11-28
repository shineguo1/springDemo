package gxj.study;

import gxj.study.config.AppConfig;
import gxj.study.zk.ZkWatcherDemo1;
import gxj.study.zk.publishSubscribePattern.MySubscriber;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
        (scanBasePackages = {"gxj.study.util"})
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(AppConfig.class)
public class SpringbootActuatorApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringbootActuatorApplication.class, args);
//		Logger.getLogger("org.apache.zookeeper.ClientCnxn").setLevel(Level.ERROR);
//		new ZkWatcherDemo1().addTreeCachhe("/");
//        String path = "/";
//        MySubscriber s = new MySubscriber("订阅者" );
//        s.register(path);
//        System.out.println("==移除订阅者1的订阅==");
    }

}
