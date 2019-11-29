package gxj.study.demo.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/11 13:31
 * @description
 */
public class Client {
    private final static String QUIT = "-quit";
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream wr =null;
        InputStream in = null;
        //读的字节流，大小限制为2048
        byte[] result = new byte[2048];
        try {
            //建立socket连接
            socket = new Socket("localhost",8888);
            System.out.println(""+socket);
            wr = socket.getOutputStream();
            in = socket.getInputStream();
            System.out.println("done connection");

            // 输入流
            Scanner sc = new Scanner(System.in);
            System.out.print("pls input:");
            String input = sc.next();

            /*
            * 循环读取输入数据，读到"-quit"退出
             */
            while(!QUIT.equals(input)) {
                if("-refresh".equals(input)){
                    //输入 -refresh 刷新server返回的数据
                    int available = in.available();
                    if(available>0) {
                        int read = in.read(result);
                        System.out.println("receive from server:" + new String(result));
                    }else{
                        System.out.println("no message");
                    }
                }
                else {
                    // 把输入写给server
                    wr.write(input.getBytes());
                }
                System.out.print("pls input:");
                input = sc.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                System.out.println("close connection");
                wr.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
