package gxj.study.dubbo.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import gxj.study.BaseTest;
import gxj.study.dubbo.spi.impl.Bumblebee;
import gxj.study.dubbo.spi.impl.Gun;
import gxj.study.dubbo.spi.impl.OptimusPrime;
import gxj.study.util.SpringContextHolder;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2020/7/13.
 */
public class RobotTest extends BaseTest{
    @Test
    public void Java_SPI_sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
    }

    @Test
    public void Dubbo_SPI_sayHello() throws Exception {
        ExtensionLoader<Robot> extensionLoader =
                ExtensionLoader.getExtensionLoader(Robot.class);
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        Bumblebee bumblebee = (Bumblebee) extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
        bumblebee.fire();
    }

    @Test
    public void Dubbo_SPI_AOP_sayHello() throws Exception {
        ExtensionLoader<AOPInterface> extensionLoader =
                ExtensionLoader.getExtensionLoader(AOPInterface.class);
        //加了wrapper，返回类型为wrapper。如有多个wrapper，规则如下：
        //1 ./META-INF/services目录下在内层，./META-INF/dubbo目录下
        //2 同目录下，写在上面的在内层，写在下面的在外层
        //3 在./META-INF/services和./META-INF/dubbo重复配置，优先读取./META-INF/dubbo里的配置。
        //wrapper判断条件：存在以传入类型type.class为参数，且只有这一个参数的构造函数。源码：ExtensionLoader.isWrapperClass()
        //使用wrapper后返回类型是wrapper代理类，不能强转成"aopImpl"具体类。
        AOPInterface aopInterface = extensionLoader.getExtension("aopImpl");
        aopInterface.sayHello();
    }

    @Test
    public void Dubbo_SPI_Weapon_sayHello() throws Exception {
        ExtensionLoader<Weapon> extensionLoader =
                ExtensionLoader.getExtensionLoader(Weapon.class);
        Gun gun = (Gun) extensionLoader.getExtension("myGun");
        gun.sayHello();
    }

    @Test
    public void Dubbo_SPI_CarMaker_sayHello() throws Exception {
        //往dubboSPI环境注入spring上下文
        SpringExtensionFactory.addApplicationContext(SpringContextHolder.getApplicationContext());

        ExtensionLoader<CarMaker> extensionLoader =
                ExtensionLoader.getExtensionLoader(CarMaker.class);
        //使用@Adaptive注解实现依赖注入，分别演示了类级别和方法级别的效果。
        CarMaker carMaker = (CarMaker) extensionLoader.getExtension("raceCarMaker");
        Map<String,String> params = new HashMap<>();
        params.put("wheelType", "michelinWheelMaker");
        params.put("engineType", "ferrariEngineMaker");
        URL url = new URL("dubbo", "192.168.0.101", 20880, params);
        carMaker.makeCar(url);
    }
}