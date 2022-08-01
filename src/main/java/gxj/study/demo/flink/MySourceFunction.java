package gxj.study.demo.flink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/22 17:31
 */
public class MySourceFunction implements SourceFunction<String> {

    /**
     * 运行标志位
     */
    private boolean isRunning = false;

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {

        isRunning = true;

        //随机数据集
        String[] names = new String[]{
                "{\"key\":\"abc\",\"time\":\"1000\",\"window\":1}\n",
                "{\"key\":\"abc\",\"time\":\"1500\",\"window\":1}\n",
                "{\"key\":\"abc\",\"time\":\"2000\",\"window\":1}\n",
                "{\"key\":\"abc\",\"time\":\"2200\",\"window\":2}\n",
                "{\"key\":\"abc\",\"time\":\"30000\",\"window\":2}\n",
                "{\"key\":\"abc\",\"time\":\"32000\",\"window\":2}\n",
                "{\"key\":\"abc\",\"time\":\"35000\",\"window\":3}\n",
                "{\"key\":\"abc\",\"time\":\"400000\",\"window\":4}\n",
        };
        int length = names.length;
        int i = 0;
        System.out.println("start send:" + new Date());
        while (isRunning & i < length) {
            //发送数据
            sourceContext.collect((names[i]));
            JSONObject jsonObject = JSON.parseObject(names[i]);
            jsonObject.put("key", "aaa");
            sourceContext.collect((jsonObject.toJSONString()));
            i = (i + 1);
            Thread.sleep(20);
        }
        //数据发送完，持续发送最后一条数据，保持stream不断
        while (isRunning) {
            sourceContext.collect((names[length - 1]));
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
