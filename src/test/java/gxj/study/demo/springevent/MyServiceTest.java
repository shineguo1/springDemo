package gxj.study.demo.springevent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xinjie_guo on 2019/11/29.
 */
@ComponentScan(basePackages = {
        "gxj.study.demo.springevent"
})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyServiceTest.class)
public class MyServiceTest {

    @Autowired
    MyService service;

    @Test
    public void test() {
        service.doSomething();
    }

}
