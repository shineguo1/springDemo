package gxj.study.demo.requestmerge;

import gxj.study.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2019/12/3.
 */
public class UserControllerTest extends BaseTest {
    @Autowired
    UserController controller;

    @Test
    public void test() {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            new java.lang.Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        User user = controller.doQuery(j % 5 + 1);
//                        System.out.println("result:"+user);
                    } catch (InterruptedException e) {

                    }
                }
            }
            ).start();
            countDownLatch.countDown();
        }
        while(true){}
    }

}