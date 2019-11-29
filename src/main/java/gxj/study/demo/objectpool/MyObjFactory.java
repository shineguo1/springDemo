package gxj.study.demo.objectpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 15:41
 * @description
 */
public class MyObjFactory extends BasePooledObjectFactory<MyObj> {
    @Override
    public MyObj create() throws Exception {
        return new MyObj();
    }

    @Override
    public PooledObject<MyObj> wrap(MyObj obj) {
        return new DefaultPooledObject<>(obj);
    }
}
