package gxj.study.demo.spring.bean;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/7 16:03
 * @description bean注入，见springframework杂记
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"gxj.study.demo.spring.bean.MyBean"};
    }
}