package gxj.study.demo.socket.file;

import java.io.*;

public class FileCopy {
    public static void main(String[] args) {
        try {
            FileInputStream inputFile = new FileInputStream("D:\\bird.jpg");
            FileOutputStream outputFile = new FileOutputStream("D:\\upload\\B.jpg");

            int bufferLength;
            byte[] buffer = new byte[1024];

            while ((bufferLength = inputFile.read(buffer)) > 0) {
                outputFile.write(buffer, 0, bufferLength);
            }

            inputFile.close();
            outputFile.close();

            System.out.println("文件复制完成！");
        } catch (IOException e) {
            System.out.println("文件复制过程中出现了错误：" + e.getMessage());
        }
    }
}