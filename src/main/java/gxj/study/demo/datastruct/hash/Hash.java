package gxj.study.demo.datastruct.hash;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/9/1 15:37
 */
public class Hash {
    /**
     * hashMap、hashset的hash算法
     */
    public static int hashInt(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
