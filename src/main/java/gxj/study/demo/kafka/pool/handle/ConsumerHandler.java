package gxj.study.demo.kafka.pool.handle;

import gxj.study.demo.kafka.pool.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li>消费者处理类</li>
 * <li>启动消费者进行消息拉取<li>
 * </ul>
 */
public class ConsumerHandler {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerHandler.class);

    /**
     * 消费者服务类
     */
    private ConsumerService consumerService;

    /**
     * 主题
     */
    private String topicName;

    /**
     * 接收到消息后,发送给"谁"进行处理
     */
    private Object receiptObj;

    /**
     * 传输的对象
     */
    private Class transObj;

    public void setConsumerService(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setReceiptObj(Object receiptObj) {
        this.receiptObj = receiptObj;
    }

    public void setTransObj(Class transObj) {
        this.transObj = transObj;
    }

    /**
     * 消费者启动执行
     * 注意:不同的消费者只允许调用1次.
     */
    public void execute() {
        logger.info("starting a consumer,topic:[{}]", topicName);
        consumerService.consumerMessages(topicName, receiptObj, transObj);
    }
}
