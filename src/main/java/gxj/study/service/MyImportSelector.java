package gxj.study.service;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/7 16:03
 * @description
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"gxj.study.service.MyBean"};
    }
}