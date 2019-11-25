package gxj.study.kafka.bean;

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
}
