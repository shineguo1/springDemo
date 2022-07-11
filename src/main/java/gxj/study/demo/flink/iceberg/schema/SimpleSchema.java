package gxj.study.demo.flink.iceberg.schema;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.data.TimestampData;
import org.apache.iceberg.Schema;
import org.apache.iceberg.types.Types;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/7 9:53
 */
public class SimpleSchema {


    public static final String TABLE_NAME = "gxj_iceberg_test";

    public static final Schema SCHEMA = new Schema(
            Types.NestedField.required(1, "id", Types.StringType.get()),
            Types.NestedField.required(2, "time_ts", Types.TimestampType.withoutZone())

            );


    public static SingleOutputStreamOperator<RowData> convertStream(DataStream<String> actionStream) {
        return actionStream.map((MapFunction<String, RowData>) s -> {
            //封装为POJO类
            GenericRowData rowData = new GenericRowData(2);
            rowData.setField(0, StringData.fromString(s));
            TimestampData time_ts = TimestampData.fromEpochMillis(System.currentTimeMillis());
            rowData.setField(1, time_ts);
            return rowData;
        });
    }

}
