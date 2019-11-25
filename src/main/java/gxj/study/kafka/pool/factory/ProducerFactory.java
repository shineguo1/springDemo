package gxj.study.kafka.pool.factory;

import gxj.study.kafka.pool.model.ProductConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <ul>
 * <li>生产者工厂类</li>
 * <li>管理生产者的创建与销毁<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
@Deprecated
public class ProducerFactory {

    private final static Logger logger = LoggerFactory.getLogger(ProducerFactory.class);

    /**
     * 生产者通道
     */
    private volatile Map<String, Producer> producerMap;

    /**
     * 生产者配置信息
     */
    private ProductConfig productConfig;

    /**
     * 初始化设置
     *
     * @param productConfig
     */
    public ProducerFactory(ProductConfig productConfig) {
        this.productConfig = productConfig;
        producerMap = new HashMap<>();
    }

    /**
     * 新增一条主题的生产通道,增加到[producerMap]中
     *
     * @param topicName
     */
    public void increase(String topicName) {
        logger.info("increase add a topic of producer instance,topic:{}", topicName);
        producerMap.put(topicName, new KafkaProducer<>(new ProductBuild().getProperty(productConfig)));
    }

    public Producer getProducer(String topicName) {
        if (producerMap.get(topicName) == null) {
            synchronized (ProducerFactory.class) {
                if (producerMap.get(topicName) == null)
                    increase(topicName);
            }
        }
        return producerMap.get(topicName);
    }

    /**
     * 服务器关闭时,主动关闭消费通道,保证通道优雅停机,防止消息丢失
     */
    public void closeAll() {
        for (Map.Entry<String, Producer> entry : producerMap.entrySet()) {
            logger.debug("close producer instance,topicName:{}", entry.getKey());
            entry.getValue().close();
        }
    }
}
