package gxj.study.demo.flink.datastream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 9:54
 */
public class DataSet_WordCount {

    public static void main(String[] args) throws Exception {
        execute();
    }

    public static void execute() throws Exception {
        //1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        /*
         使用DataSetAPI进行批处理, 1.12以后官方建议统一用DataStream API批流统一
         */

        //2. 读取数据
        String path = new FileSystemXmlApplicationContext().getResource("/src/main/java/gxj/study/demo/flink/hello.txt").getURI().getPath();
        DataSet<String> stringDataSource = env.readTextFile(path);

        //3. DataSet 批处理
        DataSet<Tuple2<String, Integer>> set =
                stringDataSource
                        //flatMap分词计数
                        .flatMap(new WordCountFlatMap())
                        //按第一个字段（word）分组
                        .groupBy(0)
                        //把第二个位置（计数）的值累加聚合
                        .sum(1);
        set.print();
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