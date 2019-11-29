package gxj.study.demo.kafka;

import gxj.study.demo.kafka.bean.TopicConst;
import gxj.study.demo.kafka.pool.factory.ProducerConnPool;
import gxj.study.demo.kafka.pool.model.ProductConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/25 10:55
 * @description
 */


public class ProductPoolDemo1 {


    /**
     * 探究为什么连接池中，producer.send(producerRecord).get()；最后需要用get阻塞?
     * 说明：producer.send()返回类型是Future，future.get()会阻塞线程，直到获取kafka broker返回的ack或异常再进行下一步。
     * <p>
     * 实验结果：不加get，消费者拿不到消息；加了get消费者拿的到消息。
     * 原因：
     * producer.send()并不是直接发送消息,而是将消息放到后端的内存中. new producer的时候会创建一个ioThread. ioThread会扫描这个缓冲区中的信息并发送.
     * "如果我们的主线程立刻退出了，而后端的IOThread还没来得及从队列中获取消息并发送给broker，这就导致了消息丢失了。"
     * <p>
     * 连接池释放连接的操作不会等到发送kafka消息完成后释放，直接释放连接会导致ioThread来不及将缓冲区中的消息发送出去，那么消息就会发送失败.
     * 所以使用连接池时需要加上get()同步操作等待发送消息(实验时发现,使用while循环发送大量消息,和使用thread.sleep休眠一会,ioThread也会有足够的时间将消息发送出去)
     * 直接使用producer.close() 时,close()方法会等待消息发送完之后再执行(会自动阻塞),所以可以不加producer.send(...).get();
     *
     *  参考博客: https://blog.csdn.net/QYHuiiQ/article/details/88757209
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ProductConfig config = new ProductConfig();
        config.setBootstrapServers("localhost:9092");

        ProducerConnPool pool = new ProducerConnPool(config);

        String topic = TopicConst.TOPIC1;
        String msg = "connPool testData ";
        Producer producer = null;
//        produce1(pool, topic, msg, producer);
//        produce2(pool, topic, msg, producer);
//        produce3(pool, topic, msg, producer);
        produce4(pool, topic, msg, producer);


        System.out.println("【结束】");

    }

    /**
     * while循环写入大量消息,ioThread来得及发送消息
     */
    private static void produce1(ProducerConnPool pool, String topic, String msg, Producer producer) {
        // 阻塞调用消息发送
        try {
            producer = pool.getProducerConn();
            System.out.println("【开始发送消息】");
            int c = 1;
            while (true) {
                Thread.sleep(1000);
                c++;
                System.out.println("【" + c + "】");

                producer.send(new ProducerRecord<String, String>(topic, msg + c), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        System.out.println("生产者【回调】 - 元数据：" + metadata + " e:" + exception);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            System.out.println("连接池【准备释放】");
            pool.releaseConn(producer);
            System.out.println("连接【关闭】");

        }
    }

    /**
    * 线程休眠一小段时间,ioThread来得及发送消息
    */
    private static void produce2(ProducerConnPool pool, String topic, String msg, Producer producer) throws InterruptedException {
        // 阻塞调用消息发送
        try {
            producer = pool.getProducerConn();
            System.out.println("【开始发送消息】");

            producer.send(new ProducerRecord<String, String>(topic, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println("生产者【回调】 - 元数据：" + metadata + " e:" + exception);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            Thread.sleep(100);
            System.out.println("连接池【准备释放】");
            pool.releaseConn(producer);
            System.out.println("连接【关闭】");
        }
    }


    /**
    * 连接池直接释放连接,ioThread来不及发送消息
    */
    private static void produce3(ProducerConnPool pool, String topic, String msg, Producer producer) throws InterruptedException {
        // 阻塞调用消息发送
        try {
            producer = pool.getProducerConn();
            System.out.println("【开始发送消息】");

            producer.send(new ProducerRecord<String, String>(topic, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println("生产者【回调】 - 元数据：" + metadata + " e:" + exception);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            System.out.println("连接池【准备释放】");
            pool.releaseConn(producer);
            System.out.println("连接【关闭】");
        }
    }

    /**
    * 生产者同步等待返回,消息发送完成后再释放连接
    */
    private static void produce4(ProducerConnPool pool, String topic, String msg, Producer producer) throws InterruptedException {
        // 阻塞调用消息发送
        try {
            producer = pool.getProducerConn();
            System.out.println("【开始发送消息】");

            producer.send(new ProducerRecord<String, String>(topic, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    System.out.println("生产者【回调】 - 元数据：" + metadata + " e:" + exception);
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            System.out.println("连接池【准备释放】");
            pool.releaseConn(producer);
            System.out.println("连接【关闭】");
        }
    }
}
