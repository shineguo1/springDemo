package gxj.study.concurrent;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/14 11:21
 * @description volatile关键字的读写指令重排
 */
public class VolatileExample2 {
    private static volatile int a = 1;
    public static void main(String[] args) throws InterruptedException {
        /**
         * 不加volatile：只有1234和3412
         *
         * 加volatile可能情况：
         * 1234：t2执行完，t1才写入cpu
         * 3412：t1执行完，t2才写入cpu
         * 3124、3142：t1、t2一起写入cpu。因为t1先运行，所以先打印了3。a的读写操作隔断了指令重排，24没有依赖顺序，可以指令重排。
         * 1324、1342：t1、t2一起写入cpu。因为t2先运行，所以先打印了1。a的读写操作隔断了指令重排，24没有依赖顺序，可以指令重排。
         */
        // 1234，1324，1342
        Thread t1 = new Thread(new Runnable() {
            String s1 = "";
            @Override
            public void run() {

                System.out.println("3");
//                a = 2;
                System.out.println("4");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("1");
//                int b =a;
                System.out.println("2");
            }
        });
        t2.start();
        t1.start();

    }
}
