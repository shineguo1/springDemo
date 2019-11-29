package gxj.study.demo.kafka;

import gxj.study.demo.kafka.bean.PropertiesUtil;
import gxj.study.demo.kafka.bean.TopicConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/25 14:52
 * @description
 */
public class SimpleConsumer {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(PropertiesUtil.getConsumerProperties());

    public void consume(Collection<String> topics) {
        consumer.subscribe(topics);
        System.out.println("【开始订阅】:" + topics);

        /*
        * 自动提交和手动提交的问题:
        * 自动提交: 如果提交延时比较短,来不及处理数据,可能会丢失数据.比如拉了100条,处理了80条就提交了,如果此时挂掉了没处理那20条数据,下一次再拉也不会拉到那20条,那20条数据就丢了.
        *           如果提交延时比较长,可能会处理重复数据. 比如设定提交间隔为5秒,花了3秒处理了100条数据,然后挂掉了,没有提交. 消费者重启后会重新拉下来这100条数据,重复处理.
        * 手动提交: 和自动提交存在相同的问题,如果处理数据后提交,当代码走到提交之前挂掉了,重启之后同样会处理重复数据;如果先提交再处理数据,但代码走到刚提交完就挂掉了,数据没有处理,
        *           重启后也不会再拉到相同的数据了. 好处是可以通过代码设定成每处理一条提交一次,缩小误差.
        *           
         */
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s%n \n",
//                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
//            consumer.commitSync();
//            }

            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    try {
                        System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s%n \n",
                                record.topic(), record.partition(), record.offset(), record.key(), record.value());
                        //同步提交
                        consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(record.offset() + 1)));
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SimpleConsumer consumer = new SimpleConsumer();

        //Arrays.asList订阅多个主题,可以订阅不存在的主题,会有警告但不会影响正常代码
        consumer.consume(Collections.singletonList(TopicConst.TOPIC1));
    }

}
