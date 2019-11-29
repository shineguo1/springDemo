package gxj.study.demo.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/18 16:00
 * @description
 */
public class ZkWatcherDemo1 {

    public  void addNodeCache(String path) throws Exception {
        CuratorFramework client = ZkConnectDemo1.getClient();
        /*
        * path不存在不会自动创建，但是可以监听不存在的节点（能监听到新建行为）
         */
        final NodeCache nodeCache = new NodeCache(client, path);
        /*
        * start(boolean b)
        * b为true，会记录缓存，下面第一行能读到data。
        * 默认: b为false，不记缓存，默认znode不存在；假如znode存在，原来有值会触发一次监听，即便值为null。
        */
        nodeCache.start(false);
        if (nodeCache.getCurrentData() != null) {
            System.out.println("nodeCache-------CurrentNode Data is:" + new String(nodeCache.getCurrentData().getData()) + "\n===========================\n");//输出当前节点的内容
        }
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() != null) {
                    System.out.println(new Date() + "nodeCache------节点数据发生了改变，发生的路径为：" + nodeCache.getCurrentData().getPath() + ",节点数据发生了改变 ，新的数据为：" + nodeCache.getCurrentData().getData());
                } else {
                    System.out.println(new Date() + "nodeCache------节点被删除");
                }
            }
        });
    }

    public void addPathChildCache(String path, PathChildrenCache.StartMode mode) throws Exception {
        CuratorFramework client = ZkConnectDemo1.getClient();
        /*
        * path不存在，会自动创建
         */
        final PathChildrenCache childCache = new PathChildrenCache(client, path, true);
        /*
        * 默认：mode 为 NORMAL 时，启动时会将所有存在子节点都触发一次监听器，类似nodeCache.start(false)
        *          打印顺序：③
        * mode 为 POST_INITIALIZED_EVENT 时，会将NORMAL的事做一遍，在所有子节点触发完监听器之后，
        *          会触发一个Type=INITIALIZED的事件，不属于任何节点，标记初始化完成。
        *          打印顺序：③ ②
        * mode 为 BUILD_INITIAL_CACHE 时，启动时不会有任何子节点触发监听器， 类似于nodeCache.start(true),
        *          但是会立即缓存对象，通过getCurrentData获取，
        *          打印顺序：①
         */
        childCache.start(mode);
        if (childCache.getCurrentData() != null) {
            childCache.getCurrentData().forEach((childData) -> {
                System.out.println("pathNodeCache-------childNode Data is:" + (childData.getData() == null ? null : new String(childData.getData())) +
                        "; path is:" + childData.getPath());// print ①
            });
        }
        childCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType() == PathChildrenCacheEvent.Type.INITIALIZED) {
                    System.out.println("初始化！");// print ②
                } else {
                    System.out.println("pathChildrenCache------发生的节点变化类型为：" + event.getType() + ",发生变化的节点内容为：" +
                            (event.getData().getData() == null ? null : new String(event.getData().getData())) +
                            ",路径：" + event.getData().getPath()); // print ③
                }
            }
        });
        //不知道作用
//        client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
//            @Override
//            public void unhandledError(String message, Throwable e) {
//                System.out.println("【error!】 message:" + message + "; detail" + e);// \
//
//            }
//        });
    }

    public void addTreeCachhe(String path) throws Exception {
        CuratorFramework client = ZkConnectDemo1.getClient();
        TreeCache treeCache = new TreeCache(client,path);
        /*
        * 只有无参start方法
         */
        treeCache.start();
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if (event.getType() == TreeCacheEvent.Type.INITIALIZED) {
                    System.out.println("初始化！");// print ②
                } else {
                    System.out.println("pathChildrenCache------发生的节点变化类型为：" + event.getType() + ",发生变化的节点内容为：" +
                            (event.getData().getData() == null ? null : new String(event.getData().getData())) +
                            ",路径：" + event.getData().getPath()); // print ③
                }
            }
        });
        client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            @Override
            public void unhandledError(String message, Throwable e) {
                System.out.println("【error!】 message:" + message + "; detail" + e);// \

            }
        });


    }

    public static void main(String[] args) throws Exception {
//        Logger.getLogger("org.apache.spark").setLevel(Level.ERROR);
//        Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF);
        Logger.getLogger("org.apache.zookeeper.ClientCnxn").setLevel(Level.ERROR);
//        new ZkWatcherDemo1().addNodeCache("/p3");
//        new ZkWatcherDemo1().addPathChildCache("/p2", PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        new ZkWatcherDemo1().addTreeCachhe("/");
        while (true) {

        }
//        ZkConnectDemo1.getClient().close();
    }
}
