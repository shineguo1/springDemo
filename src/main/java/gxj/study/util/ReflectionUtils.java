package gxj.study.util;

import java.lang.reflect.Field;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/7 11:42
 */
public class ReflectionUtils {

    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
