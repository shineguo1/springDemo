package gxj.study.demo.datastruct.hash;

import lombok.Getter;
import org.roaringbitmap.RoaringBitmap;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/9/1 13:57
 */
public class CountRoaringBitMap {

    private long size = 0;
    @Getter
    private RoaringBitmap bm = new RoaringBitmap();

    public static CountRoaringBitMap create() {
        return new CountRoaringBitMap();
    }

    public boolean add(Object o) {
        boolean b = bm.checkedAdd(hash(o));
        if (b) {
            size++;
        }
        return b;
    }

    public long size(){
        return size;
    }



    /**
     * hashMap、hashset的hash算法
     */
    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
