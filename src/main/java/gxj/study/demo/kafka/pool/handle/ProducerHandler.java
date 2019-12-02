package gxj.study.demo.kafka.pool.handle;

import gxj.study.demo.kafka.pool.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li>生产者处理类</li>
 * </ul>
 */
public class ProducerHandler {

    private final static Logger logger = LoggerFactory.getLogger(ProducerHandler.class);

    /**
     * 生产者服务类
     */
    private ProductService productService;

    /**
     * 主题
     */
    private String topicName;

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    /**
     * 执行发送消息
     *
     * @param obj
     */
    public void sendMessage(Object obj) throws Exception {
        logger.debug("topic:[{}] send a message:{}", topicName, obj.toString());
        productService.sendMessage(topicName, obj);
    }
}
