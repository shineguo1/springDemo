package gxj.study.demo.requestmerge;

import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/3 13:42
 * @description
 */
@Data
public class MergeReq<T> {

    private T data;

    private Object primaryKey;

    private String method;

    private CompletableFuture<T> future;
}
