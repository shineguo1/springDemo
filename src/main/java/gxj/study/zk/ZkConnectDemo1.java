package gxj.study.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.Random;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/18 9:28
 * @description
 */
public class ZkConnectDemo1 {

    private static String zkAddress = "localhost:2181,localhost:2182,localhost:2183";
    private static CuratorFramework client;

    public static CuratorFramework getClient() {
        if (client == null) {
            synchronized (ZkConnectDemo1.class) {
                if (client == null) {
                    client = getNewClient();
                }
            }
        }
        return client;
    }

    public static  CuratorFramework getNewClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        client.start();
        return client;
    }


    private void testCRUD() {
        CuratorFramework client = getClient();
        try {
            String s = client.create().creatingParentsIfNeeded()
//                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath("/zk/zk1/zk2", "-pData".getBytes());
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("pause");
    }


    public static void main(String[] args) {
        new ZkConnectDemo1().testCRUD();
    }

}
