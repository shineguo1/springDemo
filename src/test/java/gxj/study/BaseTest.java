package gxj.study;

//import gxj.study.demo.spring.BService;
//import gxj.study.demo.spring.MyInvocationHandler;
import gxj.study.demo.spring.bean.MyBean;
import gxj.study.util.SpringContextHolder;
//import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableAspectJAutoProxy(proxyTargetClass = false)
public class BaseTest {
//    @Autowired
//    BService bService;

	@Test
	public void contextLoads() {
//        Assert.assertEquals();
    }

//	@Test
//    public void testAspect1(){
//        bService.func1(0);
////    }

//    @Test
//    public void testAspect2() throws Exception {
//        bService.func2();
//    }
//
   @Test
    public void someTest() throws Exception {
       Object[] objectArray = new Long[1];
//       objectArray[0] = "I don't fit in"; // Throws ArrayStoreException

    }

//    @Autowired
//    MyInvocationHandler invocation;
    @Test
    public void springApplicationContextTest() throws Exception {
        ApplicationContext ac = SpringContextHolder.getApplicationContext();
//        PersonInterface p = (PersonInterface) ac.getBean("proxyService");
//        MyBean bean1 = (MyBean) ac.getBean("myBean");
        ac.getBean(MyBean.class);
//        bean1.name="@component注入";
//        MyBean bean2 = (MyBean) ac.getBean("myFactoryBean");
//        bean2.name="FactoryBean注入";
//        ac.getBean("myFactoryBean");
//        ac.getBean("&myFactoryBean");
//        PersonServiceImpl p = (PersonServiceImpl) ac.getBean("proxyService");
//        PersonServiceImpl p = new PersonServiceImpl();
//        PersonInterface proxy = (PersonInterface) Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(), p.getClass().getInterfaces(), invocation);
//        proxy.getName();
//        p.getName();
//        p.getName2();
//        p.call();
//        Object myBean = ac.getBean("myFactoryBean");
//        final Object myFactoryBean = ac.getBean("&myFactoryBean");
//        System.out.println("断点:"+p.getClass().toGenericString());
        
//        SqlSessionTemplate sqlSessionTemplate = null;
//        sqlSessionTemplate.getMapper()
//        SqlSession sqlSession=null;
//        sqlSession.getMapper()
    }

}
