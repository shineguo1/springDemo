package gxj.study.demo.threadSafeList;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/16 13:45
 */
public class ThreadSafeListDemo {

    public static void main(String[] args) {
        List vector = new Vector();
        int times = 10000000;
        List syncList = Collections.synchronizedList(Lists.newArrayList());
        List copyOnWriteList = Lists.newCopyOnWriteArrayList();
        testWrite(times, vector);
        testWrite(times, syncList);
        testWrite(100000, copyOnWriteList);
        testRead(times, vector);
        testRead(times, syncList);
        testRead(times, copyOnWriteList);
    }

    private static void testWrite(int times, List c) {
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            c.add(i);
        }
        long time2 = System.currentTimeMillis();
        System.out.println(c.getClass().getSimpleName() + " add: " + (time2 - time1));
    }

    private static void testRead(int times, List c) {
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            c.get(i % c.size());
        }
        long time2 = System.currentTimeMillis();
        System.out.println(c.getClass().getSimpleName() + " read: " + (time2 - time1));
    }
}
