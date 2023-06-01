package gxj.study.demo.socket.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadTCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(10001);
//            System.out.println("服务器已启动，等待客户端连接");
            while (true) {
                Socket socket = server.accept();
//                System.out.println("客户端已连接");

                new Thread(() -> {
                    handleRequest(socket);
                }).start();
            }
        } catch (IOException e) {
            System.out.println("启动服务器失败：" + e.getMessage());
        }
    }

    private static void handleRequest(Socket socket) {
        try (
                InputStream in = socket.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out)
        ) {
            // 读取上传文件名和文件长度
            String filename = dis.readUTF();
            Long totalLen = dis.readLong();

            // 文件保存目录
            String dir = "D:\\upload";

            // 读取内容并保存到文件中
            byte[] buffer = new byte[1024];
            int len;
            try (FileOutputStream fos = new FileOutputStream(dir + "\\" + filename)) {
//                System.out.print("文件总长度:" + totalLen);
                while (totalLen > 0 && (len = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                    totalLen -= len;
//                    System.out.print("剩余文件长度:" + totalLen);
//                    System.out.println(" 本次读取长度:" + len);
                }
            }

            // 返回上传成功信息
            dos.writeUTF("上传成功");
            dos.flush();
            dos.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("处理客户端请求失败：" + e.getMessage());
        }
    }
}
