package gxj.study.demo.flink.univ2;

import com.alibaba.fastjson.JSON;
import gxj.study.demo.flink.iceberg.WriteIcebergDemo;
import gxj.study.demo.flink.univ2.model.EventData;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.module.hive.HiveModule;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/27 17:04
 */
public class UniV2Demo {

    private static final String BOOTSTRAP_SERVER = "10.0.50.121:9092,10.0.50.122:9092,10.0.50.127:9092";

    public static void main(String[] args) throws Exception {

        /* ================ 1. 创建table环境 ================ */

        StreamTableEnvironment tableEnv = createStreamTableEnv();


         /* ================ 2. 连接kafka数据源 ================ */

        SerializableTimestampAssigner<EventData> timestampAssigner = newTimestampAssigner();

        DataStream<EventData> eventStream = readKafkaAsStream(tableEnv);
        eventStream = eventStream.filter(o -> Objects.nonNull(o.getData()) && StringUtils.isNotBlank(o.getData().getString("pairAddress")))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<EventData>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(timestampAssigner));

        /* ================ 3. 分组分窗口 ================ */
        eventStream
                .keyBy(o -> o.getData().getString("pairAddress"))
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                //清理计算过的数据
                .evictor(new TimeEvictor<TimeWindow>(1))
                .process(new EventProcessWindowFunction<>())
        ;

        //7.1 输出：命令行print
        doPrint(tableEnv);

    }

    private static SerializableTimestampAssigner<EventData> newTimestampAssigner() {
        return (element, recordTimestamp) -> {
            Long time = element.getData().getLong("currentTimestamp");
            System.out.println("[timestampAssigner] element时间戳：" + time);
            return time;
        };
    }

    private static DataStream<EventData> readKafkaAsStream(StreamTableEnvironment tableEnv) {
        Table mintTable = readFromKafka(tableEnv, "mytest_mint", "uni-v2-mint");
        Table transferTable = readFromKafka(tableEnv, "mytest_transfer", "uni-v2-transfer");
        Table syncTable = readFromKafka(tableEnv, "mytest_sync", "uni-v2-sync");
        // 连表，得到完整的数据源
        Table eventTable = mintTable.unionAll(transferTable).unionAll(syncTable);
        // table 转 stream
        return tableEnv.toDataStream(eventTable, EventData.class);
    }

    private static StreamTableEnvironment createStreamTableEnv() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 设置config
        Configuration conf = tableEnv.getConfig().getConfiguration();
        conf.setString("execution.checkpointing.interval", "60s");
        // 加载hive模块
        tableEnv.loadModule("myhive", new HiveModule("3.1.2"));
        return tableEnv;
    }

    private static void doPrint(TableEnvironment tableEnv) {
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

    public static Table readFromKafka(TableEnvironment tableEnv, String topic, String tableName) {
        tableEnv.executeSql(
                " create table IF NOT EXISTS " + tableName + " (" +
                        "data string," +
                        "name string, " +
                        "pkDay string," +
                        "project string" +
                        ") with (\n" +
                        "'connector' = 'kafka',\n" +
                        "'topic' = '" + topic + "',\n" +
                        "'properties.bootstrap.servers' = '" + BOOTSTRAP_SERVER + "',\n" +
                        "'properties.group.id' = '" + WriteIcebergDemo.class.getSimpleName() + "',\n" +
                        " 'scan.startup.mode' = 'latest-offset',\n" +
                        " 'scan.topic-partition-discovery.interval' = '10000',\n" +
                        "'format' = 'json'\n" +
                        ")\n");
        return tableEnv.from(tableName);
    }
}
