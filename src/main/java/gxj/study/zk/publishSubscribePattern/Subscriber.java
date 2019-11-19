package gxj.study.zk.publishSubscribePattern;

import gxj.study.zk.ZkConnectDemo1;
import gxj.study.zk.ZkWatcherDemo1;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.util.HashMap;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/19 9:46
 * @description
 */
@Data
public abstract class Subscriber {
//    private CuratorFramework client = ZkConnectDemo1.getClient();
//    private HashMap<String, TreeCache> registerCaches = new HashMap<>();

    public abstract void execute(ChildData data);

    public void register(String path) throws Exception {
        CuratorFramework client = ZkConnectDemo1.getClient();
        TreeCache treeCache = new TreeCache(client, path);
        /*
        * 只有无参start方法
         */
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if (event.getType() == TreeCacheEvent.Type.INITIALIZED) {
                    System.out.println("初始化！");// print ②
                } else {
                    System.out.println("pathChildrenCache------发生的节点变化类型为：" + event.getType() + ",发生变化的节点内容为：" +
                            (event.getData().getData() == null ? null : new String(event.getData().getData())) +
                            ",路径：" + event.getData().getPath()); // print ③
//                    execute(event.getData());
                }
            }
        });
//        registerCaches.put(path, treeCache);
//        new ZkWatcherDemo1().addTreeCachhe(path);
    }

//    public void remove(String path) {
//        TreeCache cache = registerCaches.get(path);
//        if (cache != null) {
//            cache.close();
//            registerCaches.remove(path);
//        }
//    }
}
