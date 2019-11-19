package gxj.study.zk.publishSubscribePattern;

import java.security.PublicKey;
import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/19 10:24
 * @description
 */
public class MyPublisher extends Publisher {

    public static void main(String[] args) throws Exception {
        MyPublisher p = new MyPublisher();
        for(int i=0;i<100;i++) {
            p.publish("/node", new Date());
            Thread.sleep(800);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
