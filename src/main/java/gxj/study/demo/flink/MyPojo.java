package gxj.study.demo.flink;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 10:32
 */
@AllArgsConstructor
@NoArgsConstructor
public class MyPojo {

    /**
     * Pojo类可以作为DataStream的泛型
     * Flink认定的POJO类型标准：
     * 1. 类是公有(public）的
     * 2. 有一个无参的构造方法
     * 3. 所有属性都是公有（public）的
     * 4. 所有属性的类型都是可以序列化的
     */

    public String name;
    public Integer age;
    public Long timestamp;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }

}
