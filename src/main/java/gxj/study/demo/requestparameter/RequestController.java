package gxj.study.demo.requestparameter;

import gxj.study.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2021/10/26 17:26
 * @description 请求参数类型汇总
 */
@Controller
public class RequestController {

    /**
     * @param request 整个request对象，包括cookie，param，attribute等属性
     * @return
     */
    @GetMapping("/getInfo")
    public String getInfo(HttpServletRequest request) {
        request.setAttribute("attribute", "abc");
        //转发到 /getInfo2, 转发不能有@ResponseBody注解
        return "forward:/getInfo2";
    }

    /**
     * @param attribute @RequestAttribute("attribute") 获取attribute属性
     * @param request 整个request对象，包括cookie，param，attribute等属性
     * @return
     */
    @ResponseBody
    @GetMapping("/getInfo2")
    public String getInfo2(@RequestAttribute("attribute") String attribute,
                            HttpServletRequest request) {
        String attribute1 = (String) request.getAttribute("attribute");
        return attribute1;
    }


    /**
     * GET http://localhost:8081/getMatrix/myPath1;id=1/myPath2;names=1;names=2
     * 矩阵变量必须紧跟路径变量才能被解析，不然会404
     *
     * @param id
     * @param names
     * @param path1
     * @param path2
     * @return
     */
    @GetMapping("/getMatrix/{path1}/{path2}")
    public String getMatrix(@MatrixVariable(value = "id", pathVar = "path1") String id,
                            @MatrixVariable(value = "names", pathVar = "path2") List<String> names,
                            @PathVariable("path1") String path1,
                            @PathVariable("path2") String path2) {
        return id;
    }

    @GetMapping("/getDemo/abc")
    @ResponseBody
    public Object getDemo(UserDTO user) {
        return user;
    }



}
