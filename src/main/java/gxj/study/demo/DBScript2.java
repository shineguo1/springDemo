package gxj.study.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2020/7/31 14:24
 * @description
 */
@Slf4j
public class DBScript2 {
    public static String readFileByBytes(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = null;
        StringBuffer sb = new StringBuffer();


        if (file.isFile() && file.exists()) { //判断文件是否存在
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(file);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                //  System.out.write(tempbytes, 0, byteread);
                String str = new String(tempbytes, 0, byteread);
                sb.append(str);
            }
        } else {
            log.info("找不到指定的文件，请确认文件路径是否正确");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String data = DBScript2.readFileByBytes("C:\\Users\\xinjie_guo\\Desktop\\data.txt");
        String[] lines = data.split("\n");
        int totalCount = 0;
        Map<String, Integer> sequenceNos = new HashMap<>();
        StringBuilder sb = new StringBuilder();
//        sb.append("UPDATE T_DATA_LEDGER_INFO SET CONTRACT_NO = CASE ID\n");
        for (String object : lines) {

            totalCount++;
            String[] split = object.split(",");
            String key = split[0];
            String mid = StringUtils.isEmpty(split[1])?split[2]:split[1];
            mid = mid.replaceAll("\"", "");
            String year = mid.split("-")[0].substring(2,4);
            String month = "00"+mid.split("-")[1];
            month = month.substring(month.length()-2);
            mid = year + month;
//            sequenceNos.putIfAbsent(mid, 0);
//            Integer num = sequenceNos.get(mid);
//            sequenceNos.put(mid, totalCount);
            String tmp = "00000" + totalCount;
            String seq = tmp.substring(tmp.length() - 5);
            sb.append("UPDATE `T_DATA_LEDGER_INFO` SET CONTRACT_NO = '").append("XYKJ-").append(mid).append("-").append(seq).append("' WHERE ID='").append(key).append("';\n");

//            sb.append("WHEN ").append(key).append(" THEN '").append("XYKJ-").append(mid).append("-").append(seq).append("'\n");
        }

        System.out.println(sb.toString());
    }
}
