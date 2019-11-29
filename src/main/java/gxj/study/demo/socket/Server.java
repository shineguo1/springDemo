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
    //IO的server
//    ServerSocket serverSocket = null;

    //NIO的server
    ServerSocketChannel serverSocket=null;
    /**
     * 缓存所有已连接的socket client
     */
    Map<String, SocketChannel> sockets = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {

        //IO 读缓冲区用字节流
        // byte[] bytes = new byte[2048];
        //NIO 读缓冲区用buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            // 创建socketChannel
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(8888));
            // 设置通道为非阻塞
            serverSocket.configureBlocking(false);
            System.out.println("wait connection");

            while (true) {
                //循环接收client连接，一旦接收到，就设为非阻塞，并纳入sockets缓存管理
                SocketChannel socket = serverSocket.accept();
                if (socket != null) {
                    String sn = "Socket@" + new Random().nextInt(100);
                    //非阻塞
                    socket.configureBlocking(false);
                    sockets.put(sn, socket);
                    System.out.println("new connection:" + sn + "源:" + socket);
                }
                //循环间隔半秒
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //遍历所有连接的socket client
                for (String key : sockets.keySet()) {
                    SocketChannel sc = sockets.get(key);
//                    System.out.println(key);
                    int read = sc.read(buffer);
                    if (read > 0) {
                        //读取缓冲区的数据
                        buffer.flip();
                        String s = Charset.forName("utf-8").decode(buffer).toString();
                        String msg = key + " - receive message:" + s;
                        System.out.println(msg);
                        //清空
                        buffer.clear();
                        //告知客户端自己收到了消息
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

    /**
     * 向所有客户端发送消息，告知自己收到了哪个客户端发的哪条消息
     * @param s
     * @throws IOException
     */
    private void sendMessage(String s) throws IOException {
        for (String key : sockets.keySet()) {
            SocketChannel sc = sockets.get(key);
            sc.write(ByteBuffer.wrap(s.getBytes()));
        }
    }


}
