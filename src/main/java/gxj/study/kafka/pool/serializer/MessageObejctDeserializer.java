package gxj.study.kafka.pool.serializer;

import gxj.study.kafka.pool.utils.CloseUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * <ul>
 * <li>对象反系列化类</li>
 * <li>User: weiwei Date:16/5/11 <li>
 * </ul>
 */

@Deprecated
public class MessageObejctDeserializer implements Deserializer {
    private static final Logger logger = LoggerFactory.getLogger(MessageObejctDeserializer.class);

    @Override
    public void configure(Map configs, boolean isKey) {
        // noting to do
    }

    @Override
    public Object deserialize(String topic, byte[] data) {

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException ex) {
            logger.error("message serializer IOException：{}", ex);
        } catch (ClassNotFoundException e) {
            logger.error("message serializer ClassNotFoundException：{}", e);
        } finally {
            CloseUtils.CloseAll(bis, ois);
        }
        return null;
    }

    @Override
    public void close() {
        // noting to do
    }
}
