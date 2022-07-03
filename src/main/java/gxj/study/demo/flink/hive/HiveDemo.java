package gxj.study.demo.flink.hive;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/24 10:33
 */

public class HiveDemo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<JSONObject> hbaseData = env.addSource(new sourceFromjson());
        hbaseData.print();
        try {
            env.execute("Flink print data");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class sourceFromjson extends RichSourceFunction<JSONObject> {
        private transient Statement st = null;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Connection con = DriverManager.getConnection("jdbc:hive2://172.20.192.58:10010/xy_ods",
                    "hadoop", "hadoop");
            st = con.createStatement();
        }

        @Override
        public void run(SourceContext<JSONObject> ctx) throws Exception {
//            System.out.println("dateTime = " + dateTime);
            ResultSet rs = st.executeQuery("SELECT  " +
                    "blocknumber" +
                    " from xy_ods.ods_nft_x2y2 limit 10");

            while (rs.next()) {
                String blocknumber = rs.getString(1);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>：" + blocknumber);
                if (StringUtils.isBlank(blocknumber)) {
                    //todo 如果为空 不插入
                    continue;
                }
                JSONObject json = new JSONObject();
                json.put("blocknumber", blocknumber);
                ctx.collect(json);
            }
        }

        @Override
        public void cancel() {

        }
    }
}
