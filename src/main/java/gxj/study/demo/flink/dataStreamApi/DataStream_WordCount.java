package gxj.study.demo.flink.dataStreamApi;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.util.Collector;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 9:54
 */
public class DataStream_WordCount {

    public static void main(String[] args) throws Exception {
        //无边界 - 流式 ： socketStream 启动 SocketServer 发送数据
        execute(DataStream_WordCount::unboundedSource);

        //有边界 - 批式：
//        execute(DataStream_WordCount::boundedSource);
    }

    public static void execute(Function<StreamExecutionEnvironment, DataStream<String>> f) throws Exception {
        //1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        /*
         使用DataSetAPI进行批处理, 1.12以后官方建议统一用DataStream API批流统一
         */

        //2. 读取数据
        DataStream<String> stringDataSource = f.apply(env);

        //3. DataSet 批处理
        DataStream<Tuple2<String, Integer>> set =
                stringDataSource
                        //flatMap分词计数
                        .flatMap(new WordCountFlatMap()).startNewChain()
                        //按第一个字段（word）分组
                        .keyBy(o -> o.f0)
                        //把第二个位置（计数）的值累加聚合
                        .sum(1);

        //4.sink输出，这里使用打印输出，同 set.print()
        set.addSink(new PrintSinkFunction<>());

        //5. 启动执行（划定界限bounded）
        env.execute();
    }

    private static DataStreamSource<String> unboundedSource(StreamExecutionEnvironment env) {
        return env.socketTextStream("127.0.0.1", 8888);
    }

    private static DataStream<String> boundedSource(StreamExecutionEnvironment env) {
        try {
            String path = new FileSystemXmlApplicationContext().getResource("/src/main/java/gxj/study/demo/flink/hello.txt").getURI().getPath();
            return env.readTextFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class WordCountFlatMap implements FlatMapFunction<String, Tuple2<String, Integer>> {
        /**
         * 按空格分词，每个词计数1
         */
        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            String[] words = s.split(" ");
            for (String word : words) {
                collector.collect(new Tuple2<>(word, 1));
            }
        }
    }
}