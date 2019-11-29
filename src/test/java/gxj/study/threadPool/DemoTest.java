package gxj.study.threadPool;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2019/11/29.
 */

public class DemoTest {
     @Test
    public void test1() throws Exception {
         new Demo().threadPoolExecutorTest1();
     }

    @Test
    public void test2() throws Exception {
        new Demo().threadPoolExecutorTest2();
    }

    @Test
    public void test3() throws Exception {
        new Demo().threadPoolExecutorTest3();
    }

    @Test
    public void test4() throws Exception {
        new Demo().threadPoolExecutorTest4();
    }

    @Test
    public void test5() throws Exception {
        new Demo().threadPoolExecutorTest5();
        //休眠一会，让线程来得及执行完
        Thread.sleep(10000);
    }

    @Test
    public void test6() throws Exception {
        new Demo().threadPoolExecutorTest6();
        //休眠一会，让线程来得及执行完
        Thread.sleep(100000);
    }

    @Test
    public void test7() throws Exception {
        new Demo().threadPoolExecutorTest7();
    }

    @Test
    public void test8() throws Exception {
        new Demo().threadPoolExecutorTest8();
    }

}