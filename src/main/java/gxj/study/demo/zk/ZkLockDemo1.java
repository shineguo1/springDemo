package gxj.study.demo.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/18 10:25
 * @description
 */
@Slf4j
public class ZkLockDemo1 {

    private String lockPath = "/zkLockDemo1/curator_lock";

    private Thread getThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                CuratorFramework client = ZkConnectDemo1.getNewClient();

                InterProcessMutex lock = new InterProcessMutex(client, lockPath);
                while(true) {
                    try {
                        if (lock.acquire(2, TimeUnit.SECONDS)) {
                            try {
                                System.out.println(Thread.currentThread() + "获得锁");
                                Thread.sleep(1000);
                            } finally {
                                lock.release();
                                System.out.println(Thread.currentThread() + "释放锁");
                            }
                        } else {
                            System.out.println(Thread.currentThread() + "失败");

                        }
                        log.info("end");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        ZkLockDemo1 demo = new ZkLockDemo1();
        for(int i=0;i<5;i++) {
            Thread t = demo.getThread();
            t.start();
        }
    }
}
