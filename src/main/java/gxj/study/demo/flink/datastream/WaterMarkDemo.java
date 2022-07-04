package gxj.study.demo.flink.datastream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.function.Function;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 9:54
 */
public class WaterMarkDemo {

    public static void main(String[] args) throws Exception {
        //无边界 - 流式 ： socketStream 启动 SocketServer 发送数据
        execute(WaterMarkDemo::unboundedSource);

        //有边界 - 批式：
//        execute(WaterMarkDemo::boundedSource);
    }

    public static void execute(Function<StreamExecutionEnvironment, DataStream<String>> f) throws Exception {
        //1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        /*
         水位线TimestampAssigner
         */
        SerializableTimestampAssigner<String> timestampAssigner = new SerializableTimestampAssigner<String>() {
            @Override
            public long extractTimestamp(String s, long l) {
                Long time = JSON.parseObject(s).getLong("time");
                System.out.println("[timestampAssigner] element时间戳：" + time);
                return time;
            }
        };

        //2. 读取数据
        DataStream<String> stringDataSource = f.apply(env);
        SingleOutputStreamOperator<String> source = stringDataSource
                .filter(StringUtils::isNotEmpty)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                        .withTimestampAssigner(timestampAssigner));

        //3. DataSet 批处理
        DataStream<JSONObject> set = source
                .map(JSON::parseObject)
                .keyBy(o -> o.getString("key"))
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .trigger(MyTimeTrigger.create())
                .process(new ProcessWindowFunction<JSONObject, JSONObject, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<JSONObject> iterable, Collector<JSONObject> collector) throws Exception {
                        System.out.println("in process, water mark:" + context.currentWatermark());
                        Iterator<JSONObject> iterator = iterable.iterator();
                        while(iterator.hasNext()) {
                            JSONObject o = iterator.next();
                            collector.collect(o);
                        }
                    }
                });
        //4.sink输出，这里使用打印输出，同 set.print()
        set.addSink(new PrintSinkFunction<>());

        //5. 启动执行（划定界限bounded）
        env.execute();
    }

    private static DataStreamSource<String> unboundedSource(StreamExecutionEnvironment env) {
        return env.socketTextStream("127.0.0.1", 8888);
    }

    private static DataStream<String> boundedSource(StreamExecutionEnvironment env) {
        try {
            String path = new FileSystemXmlApplicationContext().getResource("/src/main/java/gxj/study/demo/flink/watermark.txt").getURI().getPath();
            return env.readTextFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}