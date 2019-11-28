package gxj.study.spring.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2019/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyClassATest {
    @Autowired
    MyClassA a;

    @Test
    public void test(){
        a.test();
    }

}