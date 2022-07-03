package gxj.study.demo.flink.iceberg;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.module.hive.HiveModule;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/24 10:59
 */
public class IcebergDemo2 {

    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        TableEnvironment tableEnv = TableEnvironment.create(settings);
        Configuration conf = tableEnv.getConfig().getConfiguration();
        conf.setString("execution.checkpointing.interval", "60s");
        tableEnv.loadModule("myhive", new HiveModule("3.1.2"));
        tableEnv.executeSql("create catalog hive_catalog with ('type' = 'iceberg','catalog-type' = 'hive','hive-conf-dir'='/home/hadoop/bigdata/hive/conf')");
        tableEnv.executeSql(
                " create table IF NOT EXISTS ods_k2iceberg_univ3 (data string,name string) with\n" +
                        "(\n" +
                        "'connector' = 'kafka',\n" +
                        "'topic' = '${TopicEnum.MDPS_UNI_V3_TOPIC}',\n" +
                        "'properties.bootstrap.servers' = '${EnvConstant.BOOTSTRAP_SERVER}',\n" +
                        "'properties.group.id' = '${this.getClass.getSimpleName}',\n" +
                        " 'scan.startup.mode' = 'latest-offset',\n" +
                        " 'scan.topic-partition-discovery.interval' = '10000',\n" +
                        "'format' = 'json'\n" +
                        ")\n");

        tableEnv.executeSql("CREATE DATABASE if not exists iceberg_ods");
        tableEnv.executeSql(
                "insert into hive_catalog.iceberg_ods.testongly\n" +
                        "select\n" +
                        "name\n" +
                        "get_json_object(data,'$$.chain') as chain,\n" +
                        "get_json_object(data,'$$.project') as project,\n" +
                        "get_json_object(data,'$$.blockNo') as blockNo,\n" +
                        "get_json_object(data,'$$.poolAddress') as poolAddress,\n" +
                        "cast(get_json_object(data,'$$.currentTimestamp') as bigint) * 1000 as currentTimestamp,\n" +
                        "get_json_object(data,'$$.txHash') as txHash,\n" +
                        "get_json_object(data,'$$.logIndex') as logIndex\n" +
                        "from kafka_table");
    }


}
