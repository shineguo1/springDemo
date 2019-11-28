package gxj.study.juc;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/11/14 11:53
 * @description  线程内写指令重排证明 -》未成功
 */
public class VolatileExample3 {
    private static volatile int a = 1;

    Thread threadA = new Thread(() -> {
        MyData.getInstance().change_status();

    });

    Thread threadB = new Thread(() -> {
        MyData.getInstance().check();

    });

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //用volatile的读写操作保证threadA和treadB都有概率先获得cpu
                int b = a;
                new VolatileExample3().threadA.start();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                a = 2;
                //用volatile的读写操作保证threadA和treadB都有概率先获得cpu
                new VolatileExample3().threadB.start();
            }
        });

        // 打印 b=-1：顺序：312 或 321 或 132
        // 打印 b= 1: 顺序: 123 或 213
        // 打印 b= 0: 顺序：231 -> 证明12指令可重排，但实验没有出现
        t2.start();
        t1.start();
    }

    static class MyData {
        int a = 0;
        boolean f = false;

        public void change_status() {
            a = 1;      //指令1
            f = true;   //指令2
            //如果存在指令重排，可能导致指令 2 早于指令 1 执行，存在一种情况： f->true , a仍然等于 0 的情况下，另一线程调用 check() 得到的结果为 0 而不是 1 ；
        }

        public void check() {
            int b = -1;
            if (f) {     //指令3
                b = a;
            }
            System.out.println("result b: " + b);


        }

        private static MyData instance = new MyData();

        public static MyData getInstance() {
            return instance;
        }
    }

}
