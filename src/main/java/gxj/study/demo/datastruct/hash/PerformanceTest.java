package gxj.study.demo.datastruct.hash;

import gxj.study.util.FileUtils;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.lang3.StringUtils;
import org.roaringbitmap.RoaringBitmap;

import java.io.FileNotFoundException;
import java.util.HashSet;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/9/1 14:04
 */
public class PerformanceTest {


    public static void main(String[] args) throws FileNotFoundException {

        String fileName = "C:\\Users\\xinjie_guo\\Desktop\\UNI-V2\\V3\\ods_uniswapv3_swap_202208311659.txt";

        ZipHash zipHash = ZipHash.create(0);
        ZipBloomFilter bf = ZipBloomFilter.create(100000, 0.001, 0);
        ZipBloomFilter32 mybf = ZipBloomFilter32.create(100000, 0.001, 0);
        CountRoaringBitMap bm = CountRoaringBitMap.create();

        //读10000条数据
        HashSet<String> set = new HashSet<>();
        FileUtils.readLine(fileName, (line, index) -> {
            if (index >= 100000) {
                return;
            }
            if (StringUtils.isNotBlank(line)) {
                line = line.replace("|", "");
                set.add(line);
            }
        });
        System.out.println(set.size());
        //基于int的hashset耗时，及内存
        long t1, t2;
        t1 = System.currentTimeMillis();
        for (String s : set) {
            zipHash.add(s);
        }
        t2 = System.currentTimeMillis();
        System.out.println("intHashSet cost:" + (t2 - t1));
        System.out.println("intHashSet memory:" + ObjectSizeCalculator.getObjectSize(zipHash));
        System.out.println("intHashSet size:" + zipHash.size());

        //基于布隆过滤器的耗时及内存
        t1 = System.currentTimeMillis();
        for (String s : set) {
            bf.add(s);
        }
        t2 = System.currentTimeMillis();
        System.out.println("zipBloomFilter cost:" + (t2 - t1));
        System.out.println("zipBloomFilter memory:" + ObjectSizeCalculator.getObjectSize(bf));
        System.out.println("zipBloomFilter size:" + bf.size());

        //基于自定义简单hash布隆过滤器的耗时及内存
        t1 = System.currentTimeMillis();
        for (String s : set) {
            mybf.add(s);
        }
        t2 = System.currentTimeMillis();
        System.out.println("myBloomFilter cost:" + (t2 - t1));
        System.out.println("myBloomFilter memory:" + ObjectSizeCalculator.getObjectSize(mybf));
        System.out.println("myBloomFilter size:" + mybf.size());


        //基于RoaringBitMap及hashset的hash算法的耗时及内存
        System.out.println("hash数");
        t1 = System.currentTimeMillis();
        for (String s : set) {
            bm.add(hash(s));
        }
        t2 = System.currentTimeMillis();
        System.out.println("RoaringBitMap cost:" + (t2 - t1));
        System.out.println("RoaringBitMap memory:" + ObjectSizeCalculator.getObjectSize(bm));
        System.out.println("RoaringBitMap size:" + bm.size());


        //基于RoaringBitMap及hashset的hash算法的耗时及内存
        t1 = System.currentTimeMillis();
        RoaringBitmap m = new RoaringBitmap();
        for (int i = 0; i < 100000; i++) {
            m.add(234234234 + i);
        }
        t2 = System.currentTimeMillis();
        System.out.println("连续数");
        System.out.println("RoaringBitMap cost:" + (t2 - t1));
        System.out.println("RoaringBitMap memory:" + ObjectSizeCalculator.getObjectSize(m));

        //基于hashMap的化简版hash过滤器的耗时及内存
        t1 = System.currentTimeMillis();
        HashFilter<String> hf = new HashFilter<>();
        for (String s : set) {
            hf.put(s);
        }
        t2 = System.currentTimeMillis();
        System.out.println("HashFilter cost:" + (t2 - t1));
        System.out.println("HashFilter memory:" + ObjectSizeCalculator.getObjectSize(m));
        System.out.println("HashFilter size:" + hf.size());
    }

    /**
     * hashMap、hashset的hash算法
     */
    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
