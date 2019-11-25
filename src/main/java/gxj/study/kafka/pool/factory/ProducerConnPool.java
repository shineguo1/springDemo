package gxj.study.kafka.pool.factory;

import gxj.study.kafka.pool.model.ProductConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.clients.producer.Producer;

/**
 * <ul>
 * <li>生产者连接池</li>
 * <li>控制管理pool里的所有连接</li>
 * <li>1、构造ProducerPool池<li>
 * <li>2、获取连接<li>
 * <li>3、释放连接<li>
 * <li>User: weiwei Date:16/5/23 <li>
 * </ul>
 */
public class ProducerConnPool extends GenericObjectPool<Producer> {

    /**
     * 1、构造ProducerPool池
     */
    public ProducerConnPool(ProductConfig productConfig) {
        super(new ProducerConnFactory(productConfig), productConfig);
    }

    /**
     * 2、获取生产者连接
     *
     * @return
     * @throws Exception
     */
    public Producer getProducerConn() throws Exception {
        return super.borrowObject();
    }

    /**
     * 3、释放连接
     *
     * @param producer
     */
    public void releaseConn(Producer producer) {
        super.returnObject(producer);
    }
}