package gxj.study.demo.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/9/16 11:06
 * @description
 */
@RestController
@RequestMapping("/nacos/comsumer")
public class ConsumerConfig {
    @NacosInjected
    private NamingService namingService;


    @GetMapping(path = "/consume1")
    public String hello(@RequestParam(name = "name") String name) throws NacosException {
        // 根据服务名从注册中心获取一个健康的服务实例
        Instance instance = namingService.selectOneHealthyInstance("springDemo");
        // 这里只是为了方便才新建RestTemplate实例
        RestTemplate template = new RestTemplate();
        String url = String.format("http://%s:%d/nacos/provider/hello?name=%s", instance.getIp(), instance.getPort(),name);
        String result = template.getForObject(url, String.class);
        System.out.println(String.format("请求URL:%s,响应结果:%s", url, result));
        return result;
    }

    /** 在boot里不行，去cloud里试试*/
    @GetMapping(path = "/consume2")
    public String hello2(@RequestParam(name = "name") String name) throws NacosException {
        RestTemplate template = new RestTemplate();
        String url = String.format("http://springDemo/nacos/provider/hello?name=%s",name);
        String result = template.getForObject(url, String.class);
        System.out.println(String.format("请求URL:%s,响应结果:%s", url, result));
        return result;
    }
}
