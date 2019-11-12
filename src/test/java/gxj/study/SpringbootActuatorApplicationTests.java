package gxj.study;

//import gxj.study.service.BService;
//import gxj.study.service.MyInvocationHandler;
import gxj.study.service.MyBean;
import gxj.study.service.PersonInterface;
import gxj.study.service.PersonServiceImpl;
import gxj.study.util.SpringContextHolder;
//import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Proxy;

@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableAspectJAutoProxy(proxyTargetClass = false)
public class SpringbootActuatorApplicationTests {
//    @Autowired
//    BService bService;

	@Test
	public void contextLoads() {
	}

//	@Test
//    public void testAspect1(){
//        bService.func1(0);
////    }

//    @Test
//    public void testAspect2() throws Exception {
//        bService.func2();
//    }

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
