package gxj.study.demo.flink;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 9:42
 */
public class Environment {


    public static void main(String[] args) {

        /*
            1. 1.12版本前兼容
            静态类：
            StreamExecutionEnvironment 返回流处理环境，操作DataStream
            ExecutionEnvironment       返回批处理环境，操作DataSet
            工厂方法：
            getExecutionEnvironment：智能地根据本地运行或jar包，返回本地环境或命令提交的集群环境。
            createLocalEnvironment：返回本地环境
            createRemoteEnvironment：返回集群环境，需指定参数jobManager的地址和端口、远程运行的jar包。
         */
        StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment.createLocalEnvironment();
        StreamExecutionEnvironment.createRemoteEnvironment("localhost", 8888, "xxxx.jar", "xxxx.jar");

        ExecutionEnvironment.getExecutionEnvironment();

        /*
            2. 1.12版本后DataStream兼容流式和批式
            代码设置：(硬编码，灵活性差)
            RuntimeExecutionMode: STREAMING流式，BATCH批式，AUTOMATIC自动根据有无边界选择流式或批式。
            命令行设置：
            bin/flink runf -Dexecution.runtime-mode=BATCH ...
         */

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);

    }
}
