package gxj.study.demo.flink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 17:31
 */
public class PojoSourceFunction implements SourceFunction<MyPojo> {

    /**运行标志位*/
    private boolean isRunning = false;

    @Override
    public void run(SourceContext<MyPojo> sourceContext) throws Exception {

        isRunning = true;

        //随机数据集
        String[] names = new String[]{"eric","tom","jerry","john"};

        while(isRunning){
            //发送数据
            long timestamp = System.currentTimeMillis();
            int seed = (int) (timestamp % 1000);
            sourceContext.collect(new MyPojo(names[seed % names.length], seed % 60, timestamp));
            Thread.sleep(1000);
        }

    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
