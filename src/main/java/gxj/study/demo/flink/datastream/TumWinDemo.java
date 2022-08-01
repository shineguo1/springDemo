package gxj.study.demo.flink.datastream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gxj.study.demo.flink.MySourceFunction;
import gxj.study.demo.retry.AtomicRetryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 9:54
 */
public class TumWinDemo {

    private static AtomicRetryService bizService = new AtomicRetryService();


    public static void main(String[] args) throws Exception {
        //1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        //设置checkpoint
        env.enableCheckpointing(5 * 60 * 1000);
        // 3分钟，默认10分钟
        env.getCheckpointConfig().setCheckpointTimeout(3 * 60 * 1000L);
        // 1s
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000L);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.
                RETAIN_ON_CANCELLATION);

        // 1分钟内重启不超过5次，每次重启间隔5s，否则exception
        env.setRestartStrategy(RestartStrategies.failureRateRestart(5,
                org.apache.flink.api.common.time.Time.of(1, TimeUnit.MINUTES),
                org.apache.flink.api.common.time.Time.of(5, TimeUnit.SECONDS)));


        /*
         水位线TimestampAssigner
         */
        SerializableTimestampAssigner<String> timestampAssigner = (SerializableTimestampAssigner<String>) (s, l) -> JSON.parseObject(s).getLong("time");

        //2. 读取数据
        DataStream<String> stringDataSource = env.addSource(new MySourceFunction());
        SingleOutputStreamOperator<String> source = stringDataSource
                .filter(StringUtils::isNotEmpty)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(timestampAssigner));

        //3. DataSet 批处理
        DataStream<JSONObject> set = source
                .map(JSON::parseObject)
                .keyBy(o -> o.getString("key"))
                .window(TumblingEventTimeWindows.of(Time.seconds(1)))
                .process(new ProcessWindowFunction<JSONObject, JSONObject, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<JSONObject> iterable, Collector<JSONObject> collector) throws Exception {
                        Iterator<JSONObject> iterator = iterable.iterator();
                        while (iterator.hasNext()) {
                            JSONObject o = iterator.next();
                            if (o.getLong("time") < 1001 && s.equals("abc")) {
//                                String call = GuavaRetryHelper.<String>defaultRetry2().call(() -> bizService.doRequest("", ""));
                                bizService.doRequest("","");
                            }
                            System.out.println(JSON.toJSONString(o));
                            collector.collect(o);
                        }
                    }
                });
        //4.sink输出，这里使用打印输出，同 set.print()
//        set.addSink(new PrintSinkFunction<>());

        //5. 启动执行（划定界限bounded）
        env.execute();
    }

}