package gxj.study.demo.flink;

import com.google.common.collect.Lists;
import gxj.study.demo.flink.common.model.MyPojo;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 11:19
 */
public class Source {

    public static void main(String[] args) throws Exception {
        /*
         * 非并行数据源，设置并行度，报错 IllegalArgumentException: The parallelism of non parallel operator must be 1.
         */
//        unParallelSourceDemo();

        /*
         * 并行数据源，设置并行度2 demo
         */
        parallelSourceDemo();
    }

    private static void unParallelSourceDemo() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new PojoSourceFunction()).setParallelism(2).print();
        env.execute();
    }

    private static void parallelSourceDemo() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new PojoParallelSourceFunction()).setParallelism(2).print();
        env.execute();
    }

    public void readme(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //1. 读取socket文本流
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");
        env.socketTextStream(host, port);

        //2. 从集合读取
        env.fromCollection(Lists.newArrayList(new MyPojo(), new MyPojo(), new MyPojo()));

        //3. 从元素读取
        env.fromElements(new MyPojo(), new MyPojo(), new MyPojo());

        //4. 读取文件流
        env.readTextFile("./data.txt");

        //5. 读kafka
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");
        env.addSource(new FlinkKafkaConsumer<>("myTopic", new SimpleStringSchema(), properties));

        //6. 自定义source - 直接实现SourceFunction接口的数据源并行度只能是1
        // The parallelism of non parallel operator must be 1.
        env.addSource(new PojoSourceFunction());

        //7. 自定义并行source - 实现 ParallelSourceFunction 接口
        // 举例：FlinkKafkaConsumer 继承的 RichParallelSourceFunction
        env.addSource(new PojoParallelSourceFunction());

    }
}
