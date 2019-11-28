package gxj.study.kafka.pool.process;

import com.alibaba.fastjson.JSON;
import gxj.study.kafka.pool.utils.BizClassUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <ul>
 * <li>消费者转发</li>
 * <li>接收到消息后,转发到各个业务处理类<li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */
public class ConsumerForward {

    private final static Logger logger = LoggerFactory.getLogger(ConsumerForward.class);

    /**
     * 消费者通道对象
     */
    private KafkaConsumer consumer;

    public ConsumerForward(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public <T> void poll(String topicName, Object obj, Class<T> clas) {

        // 订阅一个主题
        consumer.subscribe(Arrays.asList(topicName));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    try {
                        //同步提交
                        consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(record.offset() + 1)));
                    } catch (Exception e) {
                        logger.warn("kafka is timeout since maybe business code processing to low:{}", e);
                        break;
                    } catch (Throwable e) {
                        logger.warn("fatal Error:kafka is timeout since maybe business code processing to low:{}", e);
                        break;
                    }

                    // 调用业务逻辑
                    try {
                        BizClassUtils.get(obj).doBiz(JSON.parseObject(record.value(), clas));
                    } catch (Exception e) {
                        logger.error("a message errors:{}", e);
                    } catch (Throwable e) {
                        logger.error("a message throwable:{}", e);
                    }
                }
            }
        }
    }
}
