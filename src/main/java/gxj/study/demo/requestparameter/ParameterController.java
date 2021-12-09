package gxj.study.demo.requestparameter;

import gxj.study.demo.spring.beanCopy.Person;
import gxj.study.model.UserDTO;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2021/10/26 17:26
 * @description 请求参数类型汇总
 */
@RestController
public class ParameterController {

    /**
     * @param id         @PathVariable Get请求，路径参数传递
     * @param name       @PathVariable Get请求，路径参数传递
     * @param userAgent  @RequestHeader("xxxxx") 取请求头中某个字段
     * @param allHeaders @RequestHeader Map类型 取所有请求头
     * @param code       @RequestParam("xxxxx") 非集合类型（如String） 请求路径?后面的参数，如?code=123
     * @param food       @RequestParam("xxxxx") 集合类型（如List） 请求路径?后面的参数，可重复。如?food=rice&food=noodles
     * @param allParams  @RequestParam Map类型 取所有请求参数，！！但是！！遇到多个重复key，如上面food的情况，只会取第一个food的value，即键值取rice，并且noodles丢失.
     * @param _ga        @CookieValue("_ga") 获取cookie值
     * @param cookieGa   @CookieValue("_ga") 获取cookie值 获取cookie对象
     * @return
     */
    @GetMapping("/demo/{id}/{name}")
    public UserDTO getUser(@PathVariable("id") String id,
                           @PathVariable("name") String name,
                           @RequestHeader("User-Agent") String userAgent,
                           @RequestHeader Map<String, String> allHeaders,
                           @RequestParam("code") String code,
                           @RequestParam("food") List<String> food,
                           @RequestParam Map<String, String> allParams,
                           @CookieValue("_ga") String _ga,
                           @CookieValue("_ga") Cookie cookieGa) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName(name);
        return user;
    }

    /**
     * @param user @RequestBody接受form或者json数据，并转换成对象。如果用String类型接受则为"k1=v1&k2=v2&k3=v3..."
     * @return
     */
    @PostMapping("/demo/post")
    public UserDTO postUser(@RequestBody UserDTO user) {
        return user;
    }



}
