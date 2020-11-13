package gxj.study.demo.juc;

public class TestSync2 implements Runnable {
    int b = 100;         
 
    synchronized void m1() throws InterruptedException {
        //m2执行晚之后m1获得锁，执行，b从2000更新为1000。 同时，主线程并行打印，不分先后。
        b = 1000;
        Thread.sleep(500); //6
        //m2线程sleep半秒后打印，必定比主线程打印的慢。且b=1000后。
        System.out.println("b=" + b);
    }
 
    synchronized void m2() throws InterruptedException {
        //m1.start后进入runnable状态，比m2后执行
        //m2先获得锁，先执行，b=2000，sleep不会释放锁
        Thread.sleep(250); //5
        b = 2000;

    }
 
    public static void main(String[] args) throws InterruptedException {
        TestSync2 tt = new TestSync2();
        Thread t = new Thread(tt);  //1
        t.start(); //2
 
        tt.m2(); //3
        //m2执行完后，主线程与m1并行执行。
        //若主线程快于m1第一句，main thread b= 2000
        //若主线程慢于m1第一句，main thread b= 1000
        System.out.println("main thread b=" + tt.b); //4
    }
 
    @Override
    public void run() {
        try {
            m1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}