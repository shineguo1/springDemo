package gxj.study.demo.spring.beanCopy;

import lombok.Builder;
import lombok.Data;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/3/30 14:36
 * @description
 */
@Data
@Builder
public class User {
    String username;

    String password;

}
