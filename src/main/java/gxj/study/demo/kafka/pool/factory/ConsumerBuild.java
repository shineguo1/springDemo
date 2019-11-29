package gxj.study.demo.kafka.pool.factory;


import gxj.study.demo.kafka.pool.model.ConsumerConfig;

import java.util.Properties;

/**
 * <ul>
 * <li>消费者构建类</li>
 * <li>保证消费者构建,防止缺胳膊少腿<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public class ConsumerBuild {

    public Properties getProperty(ConsumerConfig cc) {
        Properties props = new Properties();
        props.put("bootstrap.servers", cc.getBootstrapServers());
        props.put("group.id", cc.getGroupId());
        // TODO: 2016/11/17  因为程序消费改为了手动提交,所有这个必须是false
        props.put("enable.auto.commit","false");
        props.put("auto.commit.interval.ms", cc.getAutoCommitIntervalMs());
        props.put("session.timeout.ms", cc.getSessionTimeoutMs());
        props.put("key.deserializer", cc.getKeyDeserializer());
        props.put("value.deserializer", cc.getValueDeserializer());
        return props;
    }
}
