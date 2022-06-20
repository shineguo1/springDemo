package gxj.study.demo.flink;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/6/20 18:01
 */
public class SocketServer {
    public static void main(String[] args) throws IOException {

        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动 server ....");
            Socket s = ss.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String response = "java python c++\n";

            //每 2s 发送一次消息
            while(true){
                Thread.sleep(2000);
                bw.write(response);
                bw.flush();
                System.out.println("send");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}