package gxj.study.demo.flink;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 17:31
 */
public class PojoParallelSourceFunction extends  PojoSourceFunction implements ParallelSourceFunction<MyPojo> {

    //并行数据源只需要实现 ParallelSourceFunction 接口，代码与普通数据源相同。
    //并行数据源支持设置并行度。
}
