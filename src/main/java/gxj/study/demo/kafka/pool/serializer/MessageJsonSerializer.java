package gxj.study.demo.kafka.pool.serializer;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <ul>
 * <li>json序列化类</li>
 * </ul>
 */
public class MessageJsonSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(MessageJsonSerializer.class);

    private String encoding = "UTF8";

    @Override
    public void configure(Map configs, boolean isKey) {
        // noting to do
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            return JSON.toJSONString(data).getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            logger.debug("message serializer IOException：{}", data.toString());
        }
        return null;
    }

    @Override
    public void close() {
        // noting to do
    }
}
