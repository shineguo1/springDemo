package gxj.study.demo.flink.kafka;

import com.google.common.collect.Lists;
import gxj.study.demo.flink.common.constants.EnvConstant;
import gxj.study.demo.flink.common.constants.TopicConstant;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/27 17:04
 */
public class ReadKafkaDemo {

    private static final String KAFKA_BOOTSTRAP_TEST = "10.0.50.121:9092,10.0.50.122:9092,10.0.50.127:9092";

    public static void main(String[] args) throws Exception {
        //1. 定于环境配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 5分钟
        env.enableCheckpointing(5 * 60 * 1000L);

        // 3分钟，默认10分钟
        env.getCheckpointConfig().setCheckpointTimeout(3 * 60 * 1000L);
        // 1s
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000L);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 1分钟内重启不超过2次，每次重启间隔10s，否则exception
        env.setRestartStrategy(RestartStrategies.failureRateRestart(2,
                org.apache.flink.api.common.time.Time.of(1, TimeUnit.MINUTES),
                org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS)));


        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_TEST);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ReadKafkaDemo.class.getSimpleName());
        properties.setProperty("flink.partition-discovery.interval-millis", "5000");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        List<String> topics = Lists.newArrayList("MyTopic");
        DataStream<String> dataStream = env.addSource(new FlinkKafkaConsumer<>(topics, new SimpleStringSchema(), properties));
        dataStream.print();
        //7.1 输出：命令行print
        env.execute();
    }

    public static void createMyKafkaTable(TableEnvironment tableEnv) {
        //kafka topic
        String topic = TopicConstant.MY_TOPIC;
        //kafka节点
        String bootstrapServer = "";
        tableEnv.executeSql(
                " create table IF NOT EXISTS kafka_table (" +
                        "data string," +
                        "name string, " +
                        "pkDay string," +
                        "project string" +
                        ") with (\n" +
                        "'connector' = 'kafka',\n" +
                        "'topic' = '" + topic + "',\n" +
                        "'properties.bootstrap.servers' = '" + bootstrapServer + "',\n" +
                        "'properties.group.id' = '" + EnvConstant.KAFKA_GROUP_ID + "',\n" +
                        " 'scan.startup.mode' = 'latest-offset',\n" +
                        " 'scan.topic-partition-discovery.interval' = '10000',\n" +
                        "'format' = 'json'\n" +
                        ")\n");
    }
}
