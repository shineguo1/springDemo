package gxj.study.demo.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/9/16 11:06
 * @description
 */
//@RestController
@RequestMapping("/nacos/provider")
public class ProviderConfig {
    @NacosInjected
    private NamingService namingService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping(path = "/hello")
    public String hello(@RequestParam(name = "name") String name) {
        return String.format("%s say hello!", name);
    }

    @PostConstruct
    public void register() throws Exception {
        // 通过Naming服务注册实例到注册中心
        namingService.registerInstance(applicationName, "127.0.0.1", serverPort);
    }
}
