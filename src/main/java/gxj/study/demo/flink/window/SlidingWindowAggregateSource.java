package gxj.study.demo.flink.window;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.util.Date;
import java.util.Scanner;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 18:01
 */
public class SlidingWindowAggregateSource implements SourceFunction<SlidingWindowAggregateSource.Event> {

    /**
     * 运行标志位
     */
    private boolean isRunning = false;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {

        isRunning = true;


        //数据集
        Event[] data = new Event[]{

                /*
                [0,1000)->1
                [0,2000)->2
                [0,3000)->2
                [0,4000)->4
                [0,5000)->4
                [1000,6000)->4
                [2000,7000)->4
                 */
                new Event("Alice", 999L, 1),
                new Event("BOB", 999L, 1),
                new Event("Alice", 1100L, 1),
                new Event("Alice", 3500L, 1),
                new Event("Alice", 3600L, 1),
                new Event("Alice", 5600L, 1),
                new Event("Alice", 6600L, 1),
                new Event("Alice", 7600L, 1),
                new Event("Alice", 8600L, 1),
                new Event("BOB", 8600L, 5),
                new Event("Alice", 9600L, 1)
        };

        int length = data.length;
        int i = 0;
        System.out.println("start send:" + new Date());
        while (isRunning & i < length) {
            //发送数据
            sourceContext.collect((data[i]));
            sourceContext.emitWatermark(new Watermark(data[i].timestamp));
            i = (i + 1);
            Thread.sleep(20);
        }
        //数据发送完，保持stream不断
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Event {
        private String key;
        private Long timestamp;
        private Integer value;
        private boolean isWaterMark;

        public Event(String key, Long timestamp, Integer value){
            this.key = key;
            this.timestamp = timestamp;
            this.value = value;
        }
    }
}