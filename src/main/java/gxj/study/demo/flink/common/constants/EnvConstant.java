package gxj.study.demo.flink.common.constants;

public class EnvConstant {


    public static final String KAFKA_BOOTSTRAP_TEST = "localhost:9092";

    public static final String KAFKA_GROUP_ID = "MY-GROUP-ID";

    public static final String WAREHOUSE = "hdfs://172.20.192.57:8020/user/hive/warehouse";

    public static final String URI = "thrift://172.20.192.56:9083,thrift://172.20.192.57:9083,thrift://172.20.192.58:9083";

    public static final String CHECKPOINT_ROOT = "hdfs://ns1/flink/job/sjzt/checkpoints";

}
