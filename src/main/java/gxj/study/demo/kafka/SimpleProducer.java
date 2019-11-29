package gxj.study.demo.kafka;

import gxj.study.demo.kafka.bean.PropertiesUtil;
import gxj.study.demo.kafka.bean.TopicConst;
import lombok.Data;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

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
        producer.close();
    }

    public <T> void sendMessage(String topic, T msg) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<String, T>(topic, msg), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                System.out.println("生产者回调 - 元数据：" + metadata + " e:" + exception);
            }
        }).get();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SimpleProducer p = new SimpleProducer();
        int c = 0;
        while (true) {
            p.sendMessage(TopicConst.TOPIC1, "1620 data from api " + c);
//            Thread.sleep(1000);   //producer.send.get 设置为同步模式,会等待回调函数里的thread.sleep,这里不需要休眠了.
            c++;
        }
    }


}
