package gxj.study.demo.objectpool;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 15:41
 * @description
 */
@Data
public class MyConfig  {
    private GenericObjectPoolConfig<MyObj> poolConfig;

    MyConfig(){
        init();
    }

    private void init(){
        poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(2);
        poolConfig.setMaxTotal(4);
    }


}
