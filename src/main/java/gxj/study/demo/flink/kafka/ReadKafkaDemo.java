package gxj.study.demo.flink.kafka;

import gxj.study.demo.flink.common.constants.TopicConstant;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.module.hive.HiveModule;
import gxj.study.demo.flink.common.constants.EnvConstant;
/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/27 17:04
 */
public class ReadKafkaDemo {

    public static void main(String[] args) throws Exception {
        //1. 定于环境配置
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        //2. 创建表环境
        TableEnvironment tableEnv = TableEnvironment.create(settings);

        //3. 设置config
        Configuration conf = tableEnv.getConfig().getConfiguration();
        conf.setString("execution.checkpointing.interval", "60s");
        //4.加载hive模块
        tableEnv.loadModule("myhive", new HiveModule("3.1.2"));

        //6. 连接kafka数据源
        createMyKafkaTable(tableEnv);

        //7.1 输出：命令行print
        String createPrintOutDDL = "CREATE TABLE printOutTable (" +
                " name STRING," +
                " pk_day STRING," +
                " project STRING," +
                " chain STRING," +
                " currentTimestamp BIGINT," +
                " minter STRING" +
                ") wITH (" +
                " 'connector' = 'print'"
                + ")";
        tableEnv.executeSql(createPrintOutDDL);
        tableEnv.executeSql(
                "insert into printOutTable\n" +
                        "select\n" +
                        "name,\n" +
                        "pkDay,\n" +
                        "project,\n" +
                        "get_json_object(data,'$.chain') as chain,\n" +
                        "cast(get_json_object(data,'$.currentTimestamp') as bigint) * 1000 as currentTimestamp,\n" +
                        "get_json_object(data,'$.minter') as logIndex\n" +
                        "from kafka_table");

    }

    public static void createMyKafkaTable(TableEnvironment tableEnv) {
        //kafka topic
        String topic = TopicConstant.MY_TOPIC;
        //kafka节点
        String bootstrapServer = "";
        tableEnv.executeSql(
                " create table IF NOT EXISTS kafka_table (" +
                        "data string," +
                        "name string, " +
                        "pkDay string," +
                        "project string"+
                        ") with (\n" +
                        "'connector' = 'kafka',\n" +
                        "'topic' = '" + topic + "',\n" +
                        "'properties.bootstrap.servers' = '" + bootstrapServer + "',\n" +
                        "'properties.group.id' = '" + EnvConstant.KAFKA_GROUP_ID + "',\n" +
                        " 'scan.startup.mode' = 'latest-offset',\n" +
                        " 'scan.topic-partition-discovery.interval' = '10000',\n" +
                        "'format' = 'json'\n" +
                        ")\n");
    }
}
