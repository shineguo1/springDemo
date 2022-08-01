package gxj.study.demo.flink.datastream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 18:01
 */
public class SocketServer2 {
    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动 server ....");
            Socket s = ss.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //数据集
            String[] names = new String[]{
                    "{\"key\":\"abc\",\"time\":\"1100\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"1500\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"2000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"5000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"9800\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"10000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"19000\",\"value\":1}\n",
                    "{\"key\":\"@^%$#\",\"time\":\"20001\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"22000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"23000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"24000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"25000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"33000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"73000\",\"value\":1}\n",
                    "{\"key\":\"abc\",\"time\":\"83000\",\"value\":1}\n",
            };
            int length = names.length;
            int i = 0;
            System.out.println("start send:" + new Date());
            while (i < length) {
                //发送数据
                sendMsg(bw, names[i]);
                i = (i + 1);
                Thread.sleep(100);
            }
            //数据发送完，持续发送最后一条数据，保持stream不断
            Scanner sc = new Scanner(System.in);
            String input = sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMsg(BufferedWriter bw, String name) throws IOException {
        bw.write(name + "\n");
        bw.flush();
    }
}