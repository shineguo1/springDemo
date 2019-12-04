package gxj.study.demo.requestmerge;

import lombok.Data;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/3 15:04
 * @description
 */
@Data
public class Result<T> {
    T data;
}
