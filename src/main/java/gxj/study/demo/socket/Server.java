package gxj.study.demo.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/11 13:31
 * @description
 */
public class Server {
    ServerSocket serverSocket = null;
    Map<String, SocketChannel> sockets = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {

        byte[] bytes = new byte[2048];
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(8888));
            serverSocket.configureBlocking(false);
            System.out.println("wait connection");

            while (true) {
                SocketChannel socket = serverSocket.accept();
                if (socket != null) {
                    String sn = "Socket@" + new Random().nextInt(100);
                    socket.configureBlocking(false);
                    sockets.put(sn, socket);
                    System.out.println("new connection:" + sn);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String key : sockets.keySet()) {
                    SocketChannel sc = sockets.get(key);
//                    System.out.println(key);
                    int read = sc.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        String s = Charset.forName("utf-8").decode(buffer).toString();
                        String  msg = key + " - receive message:" + s;
                        System.out.println(msg);
                        buffer.clear();

                        sendMessage(msg);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // do close
        }
    }

    private void sendMessage(String s) throws IOException {
        for (String key : sockets.keySet()) {
            SocketChannel sc = sockets.get(key);
            sc.write(ByteBuffer.wrap(s.getBytes()));
        }
    }


}
