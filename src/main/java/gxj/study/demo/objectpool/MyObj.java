package gxj.study.demo.objectpool;

import gxj.study.util.Counter;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 15:40
 * @description
 */
public class MyObj {

    /**
     * 每次创建编号自增
     */
    private int no = Counter.getCount();

    @Override
    public String toString() {
        return "MyObj - " + no;
    }
}
