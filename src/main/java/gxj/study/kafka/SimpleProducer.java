package gxj.study.kafka;

import gxj.study.kafka.bean.PropertiesUtil;
import gxj.study.kafka.bean.TopicConst;
import lombok.Data;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.concurrent.ExecutionException;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/22 15:55
 * @description
 */
@Data
public class SimpleProducer {
    Producer producer = new KafkaProducer<>(PropertiesUtil.getProductProperties());

    @Override
    protected void finalize() {
//        producer.close();
    }

    public <T> void sendMessage(String topic, T msg) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<String, T>(topic, msg), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                System.out.println("生产者回调 - 元数据：" + metadata + " e:" + exception);
            }
        }).get();
        System.out.println("生产者连接【准备关闭】");
        producer.close();
        System.out.println("生产者连接【关闭】");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SimpleProducer p = new SimpleProducer();
        int c = 0;
//        while (true) {
            p.sendMessage(TopicConst.TOPIC1, "data from api " + c);
//            Thread.sleep(3000);
//            c++;
//        }
    }


}
