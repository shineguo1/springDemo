package gxj.study.demo.springevent;

import gxj.study.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2019/11/29.
 */
public class MyServiceTest extends BaseTest{

    @Autowired
    MyService service;

    @Test
    public void  test(){
        service.doSomething();
    }

}