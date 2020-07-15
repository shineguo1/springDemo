package gxj.study.dubbo.spi.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import gxj.study.dubbo.spi.CarMaker;
import gxj.study.dubbo.spi.EngineMaker;
import gxj.study.dubbo.spi.WheelMaker;
import gxj.study.dubbo.spi.bean.GlassMaker;
import gxj.study.dubbo.spi.model.Car;
import gxj.study.dubbo.spi.model.Engine;
import gxj.study.dubbo.spi.model.RaceCar;
import gxj.study.dubbo.spi.model.Wheel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/14 9:39
 * @description
 */
public class RaceCarMaker implements CarMaker {
    WheelMaker wheelMaker;
    EngineMaker engineMaker;
    GlassMaker glassMaker2;

    /**
     * 通过 setter 注入 AdaptiveWheelMaker，使用方法注解@Adaptive
     * 注：没有方法级别的@Adaptive标记自定义代理类，会由系统自动生成代理类
     *              ExtensionLoader.createAdaptiveExtension->ExtensionLoader.createAdaptiveExtensionClassCode
     *  生成代理类的过程中会遍历SPI接口的所有方法，检查是否存在方法级别的@Adaptive注解（用bool值hasAdaptiveAnnotation标记）
     *  如果没有任何方法存在@Adaptive注解，会抛出异常，无法生成代理类。
     *
     * @param wheelMaker
     */
    public void setWheelMaker(WheelMaker wheelMaker) {
        this.wheelMaker = wheelMaker;
    }

    /**
     * 通过 setter 注入 AdaptiveEngineMaker,使用类注解@Adaptive - 表示SPI接口的代理类
     * 注：类注解优先级高于方法注解，一个SPI接口只能有一个实现类标注@Adaptive，即一个接口只能有一个代理类
     *
     * @param engineMaker
     */

    public void setEngineMaker(EngineMaker engineMaker) {
        this.engineMaker = engineMaker;
    }

    /**
     * 通过setter 注入javaBean。优先获取beanName = glassMaker3（从方法名截取set后字符串，依据驼峰法首字母小写）。
     * 如果无法通过name获取bean，再根据类型GlassMaker.class getBean
     * 如果有2个以上的GlassMaker类型的Bean，则返回(注入)null。
     *
     * 注：需要往dubbo注入spring上下文 SpringExtensionFactory.addApplicationContext(SpringContextHolder.getApplicationContext())
     *
     * @param glassMaker
     */
    public void setGlassMaker3(GlassMaker glassMaker) {
        this.glassMaker2 = glassMaker;
    }

    @Override
    public Car makeCar(URL url) {
        Wheel wheel = wheelMaker.makeWheel(url);
        Engine engine = engineMaker.makeEngine(url);
        glassMaker2.makeGlass();
        return new RaceCar();
    }

}
