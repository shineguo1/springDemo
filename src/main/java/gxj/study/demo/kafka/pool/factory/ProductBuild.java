package gxj.study.demo.kafka.pool.factory;



import gxj.study.demo.kafka.pool.model.ProductConfig;

import java.util.Properties;

/**
 * <ul>
 * <li>生产者构建类</li>
 * <li>保证生产者构建,防止缺胳膊少腿<li>
 * </ul>
 */
public class ProductBuild {

    public Properties getProperty(ProductConfig pp) {
        Properties props = new Properties();
        props.put("bootstrap.servers", pp.getBootstrapServers().trim());
        props.put("acks", pp.getAcks());
        props.put("retries", pp.getRetries());
        props.put("batch.size", pp.getBatchSize());
        props.put("linger.ms", pp.getLingerMs());
        props.put("buffer.memory", pp.getBufferMemory());
        props.put("key.serializer", pp.getKeySerializer());
        props.put("value.serializer", pp.getValueSerializer());
        return props;
    }
}