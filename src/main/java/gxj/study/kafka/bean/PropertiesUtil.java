package gxj.study.kafka.bean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/22 16:07
 * @description
 */
public class PropertiesUtil {

    public static Properties getProductProperties(){
        Properties props = new Properties();
        //1.kafka集群
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        //2.ack应答级别
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        //3.重试次数
        props.put(ProducerConfig.RETRIES_CONFIG,"1");
        //4.批次大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,"16384");
        //5.等待时间
        props.put(ProducerConfig.LINGER_MS_CONFIG,"1");
        //6.RecordAccumulator缓冲区大小
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        //7.key序列化
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //8.value序列化
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return  props;
    }

    public static Properties getConsumerProperties(){
        Properties props = new Properties();
        //1.kafka集群
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        //2.消费者组id
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"myGroup3");
        //3.允许自动提交 : 设置成false时,不会在poll后自动更新offset,如果关掉consumer再启动之后,还是会从上次的offset读取,除非手动提交了offset.
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        //4.自动提交间隔
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        //5.key反序列化
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        //6.value反序列化
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        //7. [选填]
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
     return  props;
    }
}
