package gxj.study.demo.flink.univ2.utils;

import gxj.study.demo.flink.univ2.model.EventData;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @date 2022/5/12 18:29
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    /**
     * 安全地把集合c加进集合collection
     *
     * @param collection 主集合
     * @param c          增加的集合
     * @param <T>        集合泛型
     */
    public static <T> void addAll(Collection<T> collection, Collection<T> c) {
        if (Objects.nonNull(collection) && isNotEmpty(c)) {
            collection.addAll(c);
        }
    }

    public static <T extends Collection<R>, R> T merge(T collection, T c) {
        if (Objects.isNull(collection) && Objects.nonNull(c)) {
            return c;
        }
        if (Objects.nonNull(collection) && isNotEmpty(c)) {
            collection.addAll(c);
        }
        return collection;
    }

    public static <T> T pollFirst(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        T o = list.get(0);
        list.remove(0);
        return o;
    }
}
