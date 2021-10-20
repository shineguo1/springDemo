package gxj.study;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import gxj.study.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
        (scanBasePackages = {"gxj.study"})
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(AppConfig.class)
//关闭nacos注解
//@NacosPropertySource(dataId = "example", groupId = "",autoRefreshed = true)
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
