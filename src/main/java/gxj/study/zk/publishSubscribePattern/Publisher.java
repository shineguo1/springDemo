package gxj.study.zk.publishSubscribePattern;

import gxj.study.zk.ZkConnectDemo1;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/19 9:45
 */
@Data
public abstract class Publisher {
    private CuratorFramework client = ZkConnectDemo1.getClient();

    public void publish(String path, Object data) throws Exception {
        byte[] b = data == null ? null : data.toString().getBytes();
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, b);
        } else {
            client.setData().forPath(path, b);
        }
    }

}
