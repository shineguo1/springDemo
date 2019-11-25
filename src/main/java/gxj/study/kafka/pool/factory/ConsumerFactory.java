package gxj.study.kafka.pool.factory;

import gxj.study.kafka.pool.model.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <ul>
 * <li>消费者工厂类</li>
 * <li>管理控制消费者的数量<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public class ConsumerFactory {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerFactory.class);

    /**
     * 消费者通道队列
     */
    private volatile Map<String, KafkaConsumer> consumerMap = new HashMap<>();

    /**
     * 消费者配置信息
     */
    private ConsumerConfig consumerConfig;

    /**
     *  初始化消费者时,增加消费者配置
     * @param consumerConfig
     */
    public ConsumerFactory(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    /**
     * 新增一条主题的消费通道,添加到消费者[consumerMap]中
     *
     * @param topicName
     */
    public void increase(String topicName) {
        consumerMap.put(topicName, new KafkaConsumer<>(new ConsumerBuild().getProperty(consumerConfig)));
    }

    /**
     * 获取消费者
     * 如果该主题没有消费者通道,则创建一条该主题的通道
     *
     * @param topicName
     * @return
     */
    public KafkaConsumer getConsumer(String topicName) {
        if (consumerMap.get(topicName) == null) {
            synchronized (ConsumerFactory.class) {
                if (consumerMap.get(topicName) == null)
                    increase(topicName);
            }
        }
        return consumerMap.get(topicName);
    }

    /**
     * 服务器关闭时,主动关闭消费通道,保证通道优雅停机,防止消息丢失
     */
    public void closeAll() {
        for (Map.Entry<String, KafkaConsumer> entry : consumerMap.entrySet()) {
            logger.debug("close producer instance,topicName:{}", entry.getKey());
            entry.getValue().close();
        }
    }
}
