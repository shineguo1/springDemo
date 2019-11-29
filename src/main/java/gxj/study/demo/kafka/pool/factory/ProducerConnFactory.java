package gxj.study.demo.kafka.pool.factory;

import gxj.study.demo.kafka.pool.model.ProductConfig;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li>生产者连接工厂</li>
 * <li>User: weiwei Date:16/5/23 <li>
 * </ul>
 */
public class ProducerConnFactory extends BasePooledObjectFactory<Producer> {

    private static Logger logger = LoggerFactory.getLogger(ProducerConnFactory.class);

    private ProductConfig productConfig;

    public ProducerConnFactory(ProductConfig productConfig) {
        this.productConfig = productConfig;
    }

    /**
     * 创建一条连接
     */
    @Override
    public Producer create() throws Exception {
        logger.info("creating a Producer Connection!");
        Producer producer = new KafkaProducer<>(new ProductBuild().getProperty(productConfig));
        return producer;
    }

    /**
     * 在common-pool2中为了统计管理的对象的一些信息，比如调用次数，空闲时间，上次使用时间等，需要对管理的对象进行包装，然后在放入到对象池中
     *
     * @param obj 对象池要管理的对象
     * @return 返回包装后的PooledObject对象
     */
    @Override
    public PooledObject<Producer> wrap(Producer obj) {
        logger.debug("warp a Producer Connection!");
        return new DefaultPooledObject<Producer>(obj);
    }

}