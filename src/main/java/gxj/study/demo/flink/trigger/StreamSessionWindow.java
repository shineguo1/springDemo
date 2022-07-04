package gxj.study.demo.flink.trigger;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/7/1 10:58
 */

/**
 * @author liu a fu
 * @version 1.0
 * @date 2021/3/7 0007
 * @DESC 窗口统计案例演示：时间会话窗口（Time Session Window)，数字累加求和统计
 */
public class StreamSessionWindow {
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> inputDataStream = env.socketTextStream("127.0.0.1", 8888);

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<Tuple1<Integer>> mapDataStream = inputDataStream
                .filter(line -> null != line && line.trim().length() > 0)
                .map(new MapFunction<String, Tuple1<Integer>>() {
                    @Override
                    public Tuple1<Integer> map(String line) throws Exception {
                        return Tuple1.of(Integer.parseInt(line));
                    }
                });

        // TODO: 滚动计数窗口设置，不进行key分组，使用windowAll
        // TODO：设置会话超时时间为5秒，5秒内没有数据到来，则触发上个窗口计算
        SingleOutputStreamOperator<Tuple1<Integer>> sumDataStream = mapDataStream
                .windowAll(ProcessingTimeSessionWindows.withGap(Time.seconds(5)))
                .sum(0);

        // 4. 数据终端-sink
        sumDataStream.printToErr();

        // 5. 触发执行-execute
        env.execute(StreamSessionWindow.class.getSimpleName());
    }
}
