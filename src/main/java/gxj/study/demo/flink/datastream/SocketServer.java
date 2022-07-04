package gxj.study.demo.flink.datastream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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

            Scanner sc = new Scanner(System.in);
            System.out.print("pls input:");
            while (true) {
                String input = sc.next();
                bw.write(input + "\n");
                bw.flush();
                System.out.println("send");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}