package gxj.study.demo.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/2 9:58
 * @description 探索future.get()到底是在哪里开始阻塞
 */

public class FutureDemo {

    public void demo1() throws ExecutionException, InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(5);
        ExecutorService executor = new ThreadPoolExecutor(5, 10, 1000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

        /*
        * submit方法会将task封装成runnableFuture（FutureTask是其实现）
        * 所以本质还是futureTask和callable
         */
        Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    System.out.println("thread begin");
                    Thread.sleep(3000);
                    System.out.println("thread end");
                    System.out.println("[阻塞标记]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "thread's result";
            }
        });
        Thread.sleep(10);
        System.out.println("before code : future.get()");
        //这里会调用futureTak的get()方法
        System.out.println(submit.get() + "【在此阻塞，等待callable返回结果】");
        System.out.println("after code : future.get()");
        //关闭线程池
        executor.shutdown();
        System.out.println("线程池关闭");

    }


    public void futureTaskDemo() {
//        第一种方式：Future + ExecutorService
//        Task task = new Task();
//        ExecutorService executor = Executors.newCachedThreadPool();
//        Future<Integer> future = executor.submit(task);
//        executor.shutdown();

//        第二种方式: FutureTask + ExecutorService
//        Task task = new Task();
//        FutureTask<Integer> futureTask = new FutureTask<>(task);
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.submit(futureTask); //将其用作Runnable
//        executor.shutdown();

        //第三种方式：Thread + FutureTask
        FutureTask<String> futureTask = new FutureTask<>(new Task());
        Thread thread = new Thread(futureTask);
        thread.setName("task thread");
        System.out.println("before code [thread.start()]");
        thread.start();
        System.out.println("after code [thread.start()]");

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread " + Thread.currentThread().getName() + " is running");

        if (!futureTask.isDone()) {
            System.out.println("task is not done");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("before code [futureTask.get()]");
            //依旧是在future.get()处阻塞，等待线程运行结果
            System.out.println("task result:" + futureTask.get() + "【在此阻塞，等待线程运行结果】");
            System.out.println("after code [futureTask.get()]");
        } catch (Exception e) {

        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new FutureDemo().demo1();
        new FutureDemo().futureTaskDemo();
    }

    class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("thread start");
            Thread.sleep(5000);
            System.out.println("thread end");
            System.out.println("[阻塞标记]");
            return "task result";
        }
    }
}


