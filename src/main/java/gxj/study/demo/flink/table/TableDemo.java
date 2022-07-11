package gxj.study.demo.flink.table;

import gxj.study.demo.flink.common.model.MyPojo;
import gxj.study.demo.flink.PojoSourceFunction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/23 10:32
 */
public class TableDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 1．读取数据，得到Datastream
        SingleOutputStreamOperator<MyPojo> dataStream = env.addSource(new PojoSourceFunction())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<MyPojo>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner((SerializableTimestampAssigner<MyPojo>) (element, recordTimestamp) -> element.timestamp));


        // 2. 创建table环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 3. dataStream转table -> 流转换表
        Table table = tableEnv.fromDataStream(dataStream);

        // 4.1 写sql进行转换
        Table resultTable = tableEnv.sqlQuery("select name, age, `timestamp` from " + table);
        // 4.2 基于table转换
        Table resultTable2 = table.select($("name"), $("age"), $("timestamp"))
                .where($("name").isEqual("john"));

        // 5. 输出结果(table无法输出，转回dataStream) -> 表转换流
        tableEnv.toDataStream(resultTable).print("result1");
        // 5.1 changeLogStream，支持resultable进行聚合运算、更新操作
        tableEnv.toChangelogStream(resultTable2).print("result2  ");

        // 6. 执行
        env.execute();
    }


}