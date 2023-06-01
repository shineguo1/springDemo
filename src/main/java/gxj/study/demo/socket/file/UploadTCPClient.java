package gxj.study.demo.socket.file;


import java.io.*;
import java.net.Socket;

public class UploadTCPClient {

    public static void main(String[] args) {
        String dirPath = "D:\\";
        String filename = "bird.jpg";
        uploadFile(dirPath, filename);
    }

    private static void uploadFile(String dirPath, String filename) {
        try (
                Socket socket = new Socket("localhost", 10001);
                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                FileInputStream fis = new FileInputStream(dirPath + filename);
                BufferedInputStream bis = new BufferedInputStream(fis)
        ) {
            // 发送上传文件名和文件长度
            dos.writeUTF(filename);
            long length = (new File(dirPath + filename)).length();
//            System.out.print("fileLen:" + length);
            dos.writeLong(length);


            // 上传文件到服务器
            byte[] buffer = new byte[1024];
            int len;
            System.out.println("连接到服务器端，开始上传文件！");
//            int totalLen = 0;
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
//                totalLen += len;
//                System.out.println("Len:" + len);
            }
//            System.out.print("totalLen:" + totalLen);

            out.flush();

            // 接收服务器端的响应信息
            try (InputStream in = socket.getInputStream(); DataInputStream dis = new DataInputStream(in)) {
                String response = dis.readUTF();
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("上传文件失败：" + e.getMessage());
        }
    }
}
