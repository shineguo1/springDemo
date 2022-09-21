package gxj.study.demo.flink.iceberg;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import gxj.study.demo.flink.common.constants.EnvConstant;
import gxj.study.demo.flink.iceberg.schema.SimpleSchema;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.types.RowKind;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.flink.CatalogLoader;
import org.apache.iceberg.flink.TableLoader;
import org.apache.iceberg.flink.sink.FlinkSink;
import org.apache.iceberg.flink.source.FlinkSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/27 17:04
 */
public class SimpleReadDemo {


    public static void main(String[] args) throws Exception {

        /* ================ 1. 创建table环境 ================ */

        StreamExecutionEnvironment env = createStreamEnv();


        /* ================ 2.1 连接iceberg源 ================ */

        Map<String, String> properties = new HashMap<>();
        properties.put("warehouse", EnvConstant.WAREHOUSE);
        properties.put("uri", EnvConstant.URI);
        properties.put("property-version", "2");
        properties.put("format-version", "2");
        properties.put("write.distribution-mode", "hash");
        properties.put("write.metadata.delete-after-commit.enabled", "true");
        CatalogLoader catalogLoader = CatalogLoader.hive("hive",
                new org.apache.hadoop.conf.Configuration(), properties);
        Catalog catalog = catalogLoader.loadCatalog();



        /* ================ 4. iceberg入湖 ================ */

        PartitionSpec time = PartitionSpec.builderFor(SimpleSchema.SCHEMA).day("time_ts").build();
        TableIdentifier identifier = TableIdentifier.of("iceberg_ods", SimpleSchema.TABLE_NAME);

        org.apache.iceberg.Table table = null;
        //通过catalog判断表是否存在，不存在就创建，存在就加载
        if (!catalog.tableExists(identifier)) {
            catalog.createTable(identifier, SimpleSchema.SCHEMA, time, properties);
        } else {
            table = catalog.loadTable(identifier);
        }

        TableLoader tableLoader = TableLoader.fromCatalog(catalogLoader, identifier);

        //READ
        DataStream<RowData> flinkSource = FlinkSource.forRowData()
                .table(table)
                .tableLoader(tableLoader)
                .build();

        Set<Object> objects = Sets.newHashSet();

        flinkSource.map(new MapFunction<RowData, Object>() {
            @Override
            public Object map(RowData value) throws Exception {
                RowKind rowKind = value.getRowKind();
                StringData data = value.getString(1);
                if (rowKind == RowKind.DELETE) {
                    objects.remove(data);
                } else {
                    objects.add(data);
                }
                System.out.println(JSON.toJSONString(objects));
                return null;
            }
        });

        env.execute("SimpleTest");


    }


    private static StreamExecutionEnvironment createStreamEnv() throws IOException {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置checkpoint
        env.enableCheckpointing(6 * 60 * 1000);
        //使用中台时，指定对应checkpoint的路径
        env.setStateBackend(new RocksDBStateBackend(EnvConstant.CHECKPOINT_ROOT, true));
        // 3分钟，默认10分钟
        env.getCheckpointConfig().setCheckpointTimeout(3 * 60 * 1000L);
        // 1s
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000L);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.
                RETAIN_ON_CANCELLATION);
        return env;
    }


    private static DataStream<String> boundedSource(StreamExecutionEnvironment env) {
        return env.fromCollection(Lists.newArrayList("hello", "world", "world", "world"));
    }
}
