package gxj.study.demo.datastruct.hash;

import com.google.common.hash.Funnels;
import gxj.study.demo.datastruct.hash.my.BloomFilter32;

import java.io.Serializable;
import java.util.HashSet;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * guava的布隆过滤器是64bit，这里采用32bit哈希算法
 * 【Readme】
 * 在flink中使用bloomFilter需要配置字符编码的 KryoSerializer
 * 写法： env.getConfig().registerTypeWithKryoSerializer(Charset.forName("UTF-8").getClass(), CharsetCustomNewSerializer.class);
 *
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/8/29 14:59
 */
public class ZipBloomFilter32 implements Serializable {

    private HashSet<String> set;
    private boolean isUseBloom;
    private BloomFilter32<String> bloomFilter;
    private long expectedInsertions = 100000L;
    private double fpp = 0.0001;
    private int setLimit = 150;
    private int size = 0;

    /* ================================= 构造器 ===================================*/

    /**
     * 请使用 ZipBloomFilter.create(...)
     */
    public ZipBloomFilter32() {
        this.set = new HashSet<>();
        isUseBloom = false;
    }

    /**
     * 默认值：
     * expectedInsertions = 10000
     * fpp = 0.0001
     * setLimit = 150
     */
    public static ZipBloomFilter32 create() {
        return new ZipBloomFilter32();
    }

    /**
     * @param expectedInsertions 期望插入的数据量 must be positive
     * @param fpp                精确度（误差率） (must be positive and less than 1.0)
     * @param setLimit           插入数据超过这个阈值，数据结构从hashSet转为bloomFilter
     */
    public static ZipBloomFilter32 create(long expectedInsertions, double fpp, int setLimit) {
        ZipBloomFilter32 zipBloomFilter = new ZipBloomFilter32();
        zipBloomFilter.expectedInsertions = expectedInsertions;
        zipBloomFilter.fpp = fpp;
        zipBloomFilter.setLimit = setLimit;
        return zipBloomFilter;
    }



    /* ================================== 对外接口 ====================================*/

    public boolean put(String value) {
        return add(value);
    }

    public boolean add(String value) {
        //1.使用bloomFilter存储
        if (isUseBloom) {
            return increaseSizeIfSuccess(bloomFilter.put(value));
        }
        //2.使用hashSet存储
        if (set.size() < setLimit) {
            return increaseSizeIfSuccess(set.add(value));
        }
        //3.如果hashSet到达阈值, 存储结构转为bloomFilter
        else {
            isUseBloom = true;
            bloomFilter = initBloomFilter();
            set.forEach(o -> bloomFilter.put(o));
            set = null;
            return increaseSizeIfSuccess(bloomFilter.put(value));
        }
    }

    public boolean mightContains(String value) {
        if (isUseBloom) {
            return bloomFilter.mightContain(value);
        } else {
            return set.contains(value);
        }
    }

    public long size() {
        return size;
    }


    /* ================================== 私有方法 ================================= */

    private boolean increaseSizeIfSuccess(boolean b) {
        if (b) {
            size++;
        }
        return b;
    }

    private BloomFilter32<String> initBloomFilter() {
        return BloomFilter32.create(Funnels.stringFunnel(UTF_8), expectedInsertions, fpp);
    }

}
