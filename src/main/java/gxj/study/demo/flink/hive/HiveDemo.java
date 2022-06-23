package gxj.study.demo.flink.hive;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/23 17:02
 */
public class HiveDemo {

    public static void main(String[] args) {
        // 1 设置执行环境
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .useBlinkPlanner()
                .inStreamingMode() // 有流和批inBatchMode() 任选
                .build();

        // 2 表执行环境
        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 3 定义Hive配置
        // HiveCatalog 名称 唯一表示 随便起
        String name = "myHive";
        // 默认数据库名称，连接之后默认的数据库
        String defaultDatabase = "test";
        //hive-site.xml存放的位置，本地写本地，集群写集群的路径
        String hiveConfDir = "Conf/";

        // 4 注册
        HiveCatalog hiveCatalog = new HiveCatalog(name, defaultDatabase, hiveConfDir);
        tableEnv.registerCatalog(name,hiveCatalog);
    }
}
