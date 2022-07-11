package gxj.study.demo.flink.table;

import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/23 10:32
 */
public class TableApiDemo {
    public static void main(String[] args) throws Exception {
        //1. 定义环境配置
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        //1.1 创建表环境
        TableEnvironment tableEnv = TableEnvironment.create(settings);


        //2．创建表 SQL
        String creatDDL = "CREATE TABLE clickTable (" +
                " user_name STRING," +
                " ur1 STRING," +
                " ts BIGINT" +
                ")wITH(" +
                "'connector' = 'filesystem' ," +
                "'path' = 'input/clicks.txt' ," +
                "'format' = 'csv'" +
                ")";
        tableEnv.executeSql(creatDDL);

        //2.1 创建表 API
        Table clickTable = tableEnv.from("clickTable");
        Table resultTable = clickTable.where($("user_name").isEqual("Bob"))
                .select($("user_name"), $("ur1").cast(DataTypes.TIMESTAMP(3)));
        tableEnv.createTemporaryView("result", resultTable);

        //3. 执行SQL进行表的查询转换 SQL
        Table resultTable2 = tableEnv.sqlQuery("select url, user_name from result");

        //4. 创建输出表 SQL
        String createOutDDL = "CREATE TABLE outTable (" +
                "ur1 STRING," +
                " user_name STRING" +
                ") wITH (" +
                "'connector' = 'filesystem' ,"
                + " 'path' = 'output ' ," +
                "'format' = 'csv'" +
                ")";
        tableEnv.executeSql(createOutDDL);


        //4.1 创建控制台print表 SQL
        String createPrintOutDDL = "CREATE TABLE printOutTable (" +
                " ur1 STRING," +
                " user_name STRING" +
                ") wITH (" +
                " 'connector' = 'print'"
                + ")";
        tableEnv.executeSql(createPrintOutDDL);

        //5. execute
        resultTable2.executeInsert("outTable");
        resultTable2.executeInsert("printOutTable");


    }


}