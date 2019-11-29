package gxj.study.demo.kafka.pool;

import gxj.study.demo.kafka.pool.factory.ProducerConnPool;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * <ul>
 * <li>生产者服务类</li>
 * <li>此类封装生产者发送实体,对外提供统一入口<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    /**
     * 生产者连接池
     */
    private ProducerConnPool producerConnPool;

    public void setProducerConnPool(ProducerConnPool producerConnPool) {
        this.producerConnPool = producerConnPool;
    }

    /**
     * 从[生产者连接池]获取一条通道,然后发送[message]到broker集群
     *
     * @param topicName 主题名称
     * @param message   消息
     * @param <T>       消息类型
     */
    public <T> void sendMessage(String topicName, T message) throws Exception {
        // 连接
        Producer producer = null;

        // 阻塞调用消息发送
        try {
            producer = producerConnPool.getProducerConn();
            producer.send(new ProducerRecord<String, T>(topicName, message)).get();
        } catch (InterruptedException e) {
            logger.debug("send a message InterruptedException:", e);
            throw e;
        } catch (ExecutionException e) {
            logger.debug("send a message ExecutionException:", e);
            throw e;
        } catch (Exception e) {
            logger.error("fetch a producer connection Exception:", e);
        } finally {
            // 释放连接
            producerConnPool.releaseConn(producer);
        }
    }
}
