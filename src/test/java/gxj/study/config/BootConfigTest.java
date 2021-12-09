package gxj.study.config;

import gxj.study.BaseTest;
import gxj.study.util.SpringContextHolder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2021/10/20.
 */
public class BootConfigTest extends BaseTest{

    @Test
    public void test_configuration_proxyBeanMethods(){
        BootConfig config = SpringContextHolder.getBean("bootConfig");
        /*
         当@Configuration(proxyBeanMethods = true),BootConfig为gxj.study.config.BootConfig$$EnhancerBySpringCGLIB$$475f203c@6a5e167a
         当@Configuration(proxyBeanMethods = false)BootConfig为普通实例 gxj.study.config.BootConfig@26ca61bf

         */
        System.out.println(config);
        /*
          当@Configuration(proxyBeanMethods = true) 打印true，说明是容器单例bean
          当@Configuration(proxyBeanMethods = false) 打印false，说明是普通方法调用
         */
        System.out.println(config.userBean1() == config.userBean1());
    }

}