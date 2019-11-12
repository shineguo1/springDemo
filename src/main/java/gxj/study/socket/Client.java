package gxj.study.socket;

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
        byte[] result = new byte[2048];
        try {
            socket = new Socket("localhost",8888);
            System.out.println("done connection");
            wr = socket.getOutputStream();
            in = socket.getInputStream();
            Scanner sc = new Scanner(System.in);
            System.out.println("done connection");

            System.out.print("pls input:");
            String input = sc.next();
            while(!QUIT.equals(input)) {
                if("-refresh".equals(input)){
                    int available = in.available();
                    if(available>0) {
                        int read = in.read(result);
                        System.out.println("receive from client:" + new String(result));
                    }else{
                        System.out.println("no message");
                    }
                }
                else {
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
