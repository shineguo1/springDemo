package gxj.study.demo.kafka.pool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * <ul>
 * <li>关闭流公共包</li>
 * </ul>
 */
public class CloseUtils {
    private final static Logger logger = LoggerFactory.getLogger(CloseUtils.class);

    public static void CloseAll(Closeable... io) {
        for (Closeable temp : io) {
            if (null != temp) {
                try {
                    temp.close();
                } catch (IOException e) {
                    logger.error("close stream Exception:{}", e);
                }
            }
        }
    }
}
