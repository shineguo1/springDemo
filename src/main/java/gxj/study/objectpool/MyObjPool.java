package gxj.study.objectpool;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.ArrayList;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/28 15:41
 * @description
 */
@Data
public class MyObjPool extends GenericObjectPool<MyObj> {

    public MyObjPool() {
        super(new MyObjFactory(), new MyConfig().getPoolConfig());
    }

    public MyObj borrowObj(Long timeout) throws Exception {
        return borrowObject(timeout);
    }

    public void returnObj(MyObj o) {
        returnObject(o);
    }

    public static void main(String[] args) throws Exception {
        MyObjPool.test();

    }

    private ArrayList<MyObj> listCache = new ArrayList<>();

    private static void test() throws Exception {
        //对线池最大对象maxTotal = 4，最大空闲对象maxIdle = 2。
        MyObjPool pool = new MyObjPool();
        //初始化，清空缓存
        ArrayList<MyObj> listCache = pool.getListCache();
        listCache.clear();
        //从对象池获取五个对象。
        for (int i = 1; i <= 5; i++) {
            //设置超时5秒。
            //若超时时间小于下面的释放时间，即1秒，会获取不到对象抛出异常。
            MyObj o = pool.borrowObj(5000L);
            //打印对象的编号
            System.out.println(o.toString());
            //用缓存管理对象
            listCache.add(o);
            if (i == 3) {
                //第三个对象将在一秒后释放。
                new Thread(pool.releaseAfterOneSecond(o)).start();
            }
        }
        //释放所有连接
        listCache.forEach(o->{
            System.out.println(o.toString()+" 释放");
            pool.returnObj(o);
        });
        //从对象池获取4个对象。
        //已知：超过最大空闲数的对象在归还给对象池时会被销毁。
        //因为最大空闲对象只有2个，所以前两个对象是优先释放的，后两个对象是新创建的。
        for (int i = 1; i <= 4; i++) {
            //设置超时5秒。
            MyObj o = pool.borrowObj(5000L);
            //打印对象的编号
            System.out.println(o.toString());
            //用缓存管理对象
            listCache.add(o);
        }
        System.out.println("end");
    }

    private Runnable releaseAfterOneSecond( MyObj o) {
        MyObjPool p = this;
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(o.toString()+" 即将释放");
            p.returnObject(o);
            listCache.remove(o);
        };
    }
}
