package gxj.study.demo.flink.iceberg.util;

import org.apache.flink.table.data.StringData;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/8 17:16
 */
public class StringDataUtils {


    /** Creates an instance of {@link StringData} from the given {@link String}. */
    public static StringData fromString(String str) {
        return StringData.fromString(str);
    }

    /** Creates an instance of {@link StringData} from the given UTF-8 byte array. */
    public static StringData fromBytes(byte[] bytes) {
        return StringData.fromBytes(bytes);
    }

    /**
     * Creates an instance of {@link StringData} from the given UTF-8 byte array with offset and
     * number of bytes.
     */
    public static StringData fromBytes(byte[] bytes, int offset, int numBytes) {
        return StringData.fromBytes(bytes, offset, numBytes);
    }

    public static StringData fromBigDecimal(BigDecimal decimal){
        return Objects.isNull(decimal) ? null : StringData.fromString(decimal.toString());
    }
}
