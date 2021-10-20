package gxj.study.demo.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


//TODO: 关闭nacos
//@RestController
@RequestMapping("/config")
public class ConfigController {

    /**能设置默认值，能配置自动刷新*/
    @NacosValue(value = "${key2:abc}", autoRefreshed = true)
    private String nacosValue;

    @Value(value = "${key1}")
    private String value;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get() {
        return  String.format("{\"@NacosValue\":%s, \"@Value\":%s}", nacosValue,value);
    }
}