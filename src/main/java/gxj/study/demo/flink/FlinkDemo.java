package gxj.study.demo.flink;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;

import java.io.IOException;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/17 16:57
 */
public class FlinkDemo {

    public static void main(String[] args) throws Exception {
        execute();
    }

    public static void execute() throws Exception {
        //1. 创建*流*执行环境 - StreamExecutionEnvironment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TypeSerializer<Integer> serializer = TypeInformation.of(Integer.class).createSerializer(new ExecutionConfig());
        DataStream<Integer> dataStream = env.addSource(new FromElementsFunction<>(serializer, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Types.INT);
        DataStream<Integer> output = dataStream
                .map(o -> o * 1)
                .keyBy(o -> 1)
                .keyBy(o -> o.hashCode() % 2 + 1)
                .sum(0);

        //写入打印
        output.addSink(new PrintSinkFunction<>());
        System.out.println(env.getExecutionPlan());

        //执行
        System.out.println("execute:");
        env.execute();

    }
}
