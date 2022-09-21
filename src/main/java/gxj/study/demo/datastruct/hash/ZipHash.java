package gxj.study.demo.datastruct.hash;

import java.util.HashSet;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/8/29 14:59
 */
public class ZipHash {

    private HashSet<String> set;
    private HashSet<Integer> intSet;
    private boolean isZip;
    private int setLimit = 150;

    /* ================================= 构造器 ===================================*/

    /**
     * 请使用 ZipHash.create(...)
     */
    private ZipHash() {
        this.set = new HashSet<>();
        isZip = false;
    }

    public static ZipHash create() {
        return new ZipHash();
    }

    public static ZipHash create(int limit) {
        ZipHash zipHash = new ZipHash();
        zipHash.setLimit = limit;
        return zipHash;
    }

    /* ================================== 对外接口 ====================================*/

    public boolean put(String value) {
        return add(value);
    }

    public boolean add(String value) {
        //1.使用bloomFilter存储
        if (isZip) {
            return intSet.add(hash(value));
        }
        //2.使用hashSet存储
        if (set.size() < setLimit) {
            return set.add(value);
        }
        //3.如果hashSet到达阈值, 存储结构转为bloomFilter
        else {
            isZip = true;
            intSet = new HashSet<>();
            set.forEach(o -> intSet.add(hash(o)));
            set = null;
            return intSet.add(hash(value));
        }
    }

    public boolean mightContains(String value) {
        if (isZip) {
            return intSet.contains(hash(value));
        } else {
            return set.contains(value);
        }
    }

    public long size() {
        if (isZip) {
            return intSet.size();
        } else {
            return set.size();
        }
    }

    /* ================================== 私有方法 ================================= */
    /**
     * hashMap、hashset的hash算法
     */
    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
