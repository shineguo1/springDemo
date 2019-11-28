package gxj.study.spring.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/7 15:31
 * @description
 */

public class MyBean{
    public MyBean(){
        System.out.println("MyBean构造方法");
    }
    public String name = "init";
}