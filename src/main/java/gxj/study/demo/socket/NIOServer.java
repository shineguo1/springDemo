package gxj.study.demo.socket;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) {
        try {
            //创建服务端，设置非阻塞
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress("localhost", 8888));
            ssc.configureBlocking(false);

            //创建选择器
            Selector selector = Selector.open();
            // 注册 server channel，并且指定感兴趣的事件是 Accept
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            //读写缓冲
            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(128);
            writeBuff.put("received".getBytes());
            writeBuff.flip();

            while (true) {
                //底层掉了这个native方法，cpu收到消息后会通知java程序：sun.nio.ch.WindowsSelectorImpl.SubSelector.poll0()
                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                /*
                * SelectionKey 有interestOps和readyOps
                * interestOps记录key感兴趣的事件
                * readyOps记录key触发的事件
                * isAcceptable、isReadable等函数都是根据readyOps计算的
                * 事件采用的是位运算，不同事件的位不同：read=1，write=4，connect=8，accept=16
                 */
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        // 说明cpu接受到了新的client连接
                        // 创建新的连接，并且把连接注册到selector
                        // 声明这个channel只对读操作感兴趣。
                        SocketChannel socketChannel = ssc.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("new connection:" + socketChannel);
                    }
                    if (key.isReadable()) {
                        // 读缓冲区非空，有可读数据
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        readBuff.clear();
                        socketChannel.read(readBuff);
                        readBuff.flip();
                        System.out.println("received : " + new String(readBuff.array()));
                        // 设置对写事件感兴趣，发送回复信息 （这里的key是发消息的client的channel）
                        key.interestOps( SelectionKey.OP_WRITE);

                    }
                    if (key.isWritable()) {
                        // 写缓冲区未满，可写入数据
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        String msg = socketChannel + "received";
                        // 回复消息 - 只针对发消息的channel（因为针对发消息的client设置了写操作感兴趣）
                        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
                        System.out.println("send message : " + msg);
                        // 回复完消息，继续监听client的消息
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}