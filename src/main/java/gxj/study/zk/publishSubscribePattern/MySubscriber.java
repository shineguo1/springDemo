package gxj.study.zk.publishSubscribePattern;

import com.google.common.hash.BloomFilter;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/19 10:21
 * @description
 */
public class MySubscriber extends Subscriber {
    String name;

    public MySubscriber(String name) {
        this.name = name;
    }

    @Override
    public void execute(ChildData data) {
        String d = data.getData() == null ? "null" : new String(data.getData());
        String path = data.getPath();
        System.out.println(name + " - handle 路径:" + path + "; 数据:" + d);
    }

    public static void main(String[] args) throws Exception {
        List<MySubscriber> list = new ArrayList<>();
        String path = "/";
        for(int i=0;i<1;i++) {
            MySubscriber s = new MySubscriber("订阅者"+i);
            s.register(path);
            list.add(s);
        }
//        Thread.sleep(8000);
//        list.get(0).remove(path);
        System.out.println("==移除订阅者1的订阅==");
        Thread.sleep(Integer.MAX_VALUE);

    }
}
