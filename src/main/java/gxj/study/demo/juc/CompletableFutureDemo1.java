package gxj.study.demo.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2019/12/2 11:25
 * @description
 */
public class CompletableFutureDemo1 {
    /**
     * CompletableFuture的静态工厂方法 (4个):
     * runAsync(Runnable runnable)	使用ForkJoinPool.commonPool()作为它的线程池执行异步代码。
     * runAsync(Runnable runnable, Executor executor)	使用指定的thread pool执行异步代码。
     * supplyAsync(Supplier<U> supplier)	使用ForkJoinPool.commonPool()作为它的线程池执行异步代码，异步操作有返回值
     * supplyAsync(Supplier<U> supplier, Executor executor)	使用指定的thread pool执行异步代码，异步操作有返回值
     * <p>
     * PS: 在不指定线程池情况下，ForkJoinPool.commonPool()是默认线程池
     * Reference：https://blog.csdn.net/u012129558/article/details/78962759
     * <p>
     * <p>
     * 1.runAsync 和 supplyAsync 方法的区别是runAsync返回的CompletableFuture是没有返回值的。
     */
    public void runAsyncDemo() throws ExecutionException, InterruptedException {
        System.out.println("【runAsync】");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("[in thread] print Hello"));

//        可以指定线程池：
//        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> System.out.println("[in thread] print Hello"),
//                new ThreadPoolExecutor(5, 10, 1000,TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>()));

        System.out.println("result:" + future.get() + "\n");
    }

    /**
     * 2. 而supplyAsync返回的CompletableFuture是由返回值的，下面的代码打印了future的返回值。
     */
    public void supplyAsyncDemo() throws ExecutionException, InterruptedException {
        System.out.println("【supplyAsync】");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("[in thread] return Hello");
            return "Hello";
        });
//        可以指定线程池：
//        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("[in thread] return Hello");
//            return "Hello";
//        }, new ThreadPoolExecutor(5, 10, 1000,
//                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>()));
        System.out.println("result:" + future.get() + "\n");
    }


    /**
     * complete(T t)	完成异步执行，并返回future的结果
     * completeExceptionally(Throwable ex)	异步执行不正常的结束
     * <p>
     * <p>
     * 1. 先执行complete，后返回线程结果
     * 1.2 多次complete，只有第一次生效
     * 期望：future.get() 拿到的是第一次complete的值
     */
    public void completeDemo1() throws ExecutionException, InterruptedException {
        System.out.println("【complete】complete先于线程执行完");
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("[in thread] start sleep");

            sleep(500);
            System.out.println("[in thread] return Hello //返回值无效");
            return "Hello";
        });
        Thread.sleep(10);
        System.out.println("[in main] complete World");
        future.complete("[complete] World");
        System.out.println("[in main] complete World2");
        future.complete("[complete2] World2");
        System.out.println("result:" + future.get() + " -- //这里看不到[in thread]的打印，complete mock了future的返回，且只有第一次complete有效，但线程会执行完");
        System.out.println("[in main] 这里sleep一秒，确保主线程会在线程结束之后关闭，验证线程不会被complete中断，只是被改写了返回值:");
        Thread.sleep(1000);
        System.out.println("现在的result:" + future.get() + "\n");

    }

    /**
     * 2. 先返回线程结果，后执行complete
     * 期望：future.get() 拿到的是线程的返回值
     */
    public void completeDemo2() throws ExecutionException, InterruptedException {
        System.out.println("【complete】线程先于complete返回结果");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("[in thread] return Hello");
            return "Hello";
        });
        Thread.sleep(1000);
        System.out.println("[in main] complete World");
        future.complete("[complete] World");

        System.out.println("result:" + future.get() + "\n");
    }

    public void completeDemo3() throws ExecutionException, InterruptedException {
        System.out.println("【completeExceptionally】completeExceptionally先于线程执行");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[in thread] start sleep");
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
            System.out.println("[in thread] return Hello");
            return "Hello";
        });
        Thread.sleep(10);
        System.out.println("[in main] completeExceptionally");
        future.completeExceptionally(new Exception("completeExceptionally抛出的异常"));
        try {
            System.out.println("result:" + future.get() + "\n");
        } catch (Exception e) {
            System.out.println("[in catch] result: " + e.getMessage());
        }
    }

    /**
     * CompletionStage接口方法：同上文，Async结尾的方法是异步执行的，允许指定线程池,非Async结尾方法是在主线程中做的。
     * supplyAsync返回结果不受影响，apply(combine),accept,run的区别是：
     * apply(combine): 能拿到supplyAsync的返回值，能有自己的返回值（返回值增强/包装）。
     * accept：能拿到supplyAsync的返回值，无返回值
     * run：不能拿到supplyAsync的返回值，无返回值
     *
     * 一、单一结果
     * 1. 对结果进行转换
     * public <U> CompletionStage<U> thenApply(Function<? super T,? extends U> fn);
     * public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn);
     * public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn,Executor executor);
     *
     * 2. 对结果进行消耗，在action中处理结果，无返回值 （supplyAsync仍有返回结果） 和3的区别：能拿到supplyAsync的返回值
     * public CompletionStage<Void> thenAccept(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
     *
     * 3. 对上一步的计算结果不关心，执行下一个操作，无返回值（supplyAsync仍有返回结果） 和2的区别：不能拿到supplyAsync的返回值
     * public CompletionStage<Void> thenRun(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
     *
     * 二、两个结果，等待全部执行完 (两个线程异步（并行))
     * 4. 结合两个CompletionStage的结果，进行转化后返回
     * public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
     *
     * 5. 结合两个CompletionStage的结果，进行消耗
     * public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action,     Executor executor);
     *
     * 6. 在两个CompletionStage都运行完执行。不关心这两个CompletionStage的结果，只关心这两个CompletionStage执行完毕，之后在进行操作（Runnable）
     * public CompletionStage<Void> runAfterBoth(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor);
     *
     * 三、两个结果，任意一个执行完（即取快的）
     * 7. 两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的转化操作。
     * public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn,Executor executor);
     *
     * 8. 两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的消耗操作。
     * public CompletionStage<Void> acceptEither(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action,Executor executor);
     *
     * 9. 两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）。
     * public CompletionStage<Void> runAfterEither(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor);
     *
     * 四、异常处理
     * 10. 当运行时出现了异常，可以通过exceptionally进行补偿
     * public CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn);
     *
     * 11. 当运行完成时，对结果的记录。这里的完成时有两种情况，一种是正常执行，返回值。另外一种是遇到异常抛出造成程序的中断。
     * 这里为什么要说成记录，因为这几个方法都会返回CompletableFuture，当Action执行完毕后它的结果返回原始的CompletableFuture的计算结果或者返回异常。
     * 不会对结果产生任何的作用（不会也不能修改结果）。
     * public CompletionStage<T> whenComplete(BiConsumer<? super T, ? super Throwable> action);
     * public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action);
     * public CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action,Executor executor);
     *
     * 12. 运行完成时，对结果的处理。这里的完成时有两种情况，一种是正常执行，返回值。另外一种是遇到异常抛出造成程序的中断。
     * 可以修改返回结果
     * public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);
     * public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn);
     * public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn,Executor executor);
     */


    /**
     * join()与get()
     * join能抛出unchecked异常，get只能抛出具体的异常
     */

    /**
     * 1.thenApply 单一转换
     * 1.1 同步 - apply在主线程中做做
     */
    public void thenApplyDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = CompletableFuture.supplyAsync((Supplier<Object>) () -> {
            System.out.println(Thread.currentThread().getName() + "[in thread] return hello");
            return "hello";
        }).thenApply(s -> {
            System.out.println(Thread.currentThread().getName() + "[in theApply] start sleep");
            sleep(100);
            System.out.println(Thread.currentThread().getName() + "[in theApply] return [result] + world");
            return s + " world";
        });
        System.out.println("[in main] someCode //与thenApply同步（串行）");
        System.out.println("result:" + future.join());
    }

    /**
     * 1.2 异步 - apply在指定线程中做
     */
    public void thenApplyAsyncDemo() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = CompletableFuture.supplyAsync((Supplier<Object>) () -> {
            System.out.println(Thread.currentThread().getName() + "[in thread] return hello");
            return "hello";
        }).thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName() + "[in theApply] start sleep");
            sleep(100);
            System.out.println(Thread.currentThread().getName() + "[in theApply] return [result] + world");
            return s + " world";
        }, Executors.newFixedThreadPool(1));
        sleep(10);
        System.out.println("[in main] someCode //thenApplyAsync异步（并行）");
        System.out.println("result:" + future.get());
    }

    /**
     * 2.thenAccept 单一消费 无返回值 （supplyAsync仍有返回结果）
     */

    public void thenAcceptDemo() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<Void> future1 = future.thenAccept(s -> System.out.println(s + " world //在此消费结果，能拿到hello"));
        System.out.println("result1:" + future.join() + " //supplyAsync返回值");
        System.out.println("result2:" + future1.join() + " //thenAccept无返回值\n");
    }

    /**
     * 3. thenRun 无返回结果、不关心结果直接进行操作（supplyAsync仍有返回结果）
     */
    public void thenRunAsync() {
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            System.out.println(Thread.currentThread().getName() + "[in supplyAsync] return hello");
            return "hello";
        });
        CompletableFuture<Void> future2 = future1.thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "[in thenRun] start sleep");
            sleep(100);
            System.out.println(Thread.currentThread().getName() + "[in thenRun] println world //拿不到hello");
        }, Executors.newFixedThreadPool(3));
        sleep(10);
        System.out.println("[in main] someCode //thenRunAsync异步（并行）");
        System.out.println("result:" + future1.join() + " //supplyAsync返回值");
        System.out.println("result:" + future2.join() + " //thenRunAsync无返回值\n");
    }

    /**
     * 4. thenCombine 结合两个结果，进行转化后返回 (两个线程异步（并行))
     */
    public void thenCombineDemo() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            println("[in supplyAsync] hello");
            return "hello";
        });
        CompletableFuture<String> future2 = future.thenCombine(CompletableFuture.supplyAsync(() -> {
            sleep(100);
            println("[in thenCombine - supplyAsync] world");
            return "world";
        }), (s1, s2) -> s1 + " " + s2);
        System.out.println("result:" + future.join() + " //supplyAsync");
        System.out.println("result:" + future2.join() + " //thenCombine");
    }

    /**
     * 5. thenAcceptBoth 结合两个结果，进行消费 (两个线程异步（并行))
     */
    public void thenAcceptBothDemo() {
        CompletableFuture.supplyAsync(() -> {
            sleep(100);
            println("[in supplyAsync] hello");
            return "hello";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            sleep(100);
            println("[in thenAcceptBoth  - supplyAsync ] world");
            return "world";
        }), (s1, s2) -> System.out.println(s1 + " " + s2));
        sleep(200);
    }

    /**
     * 7. applyToEither 两个线程，谁运行的快，返回谁的结果进行转换
     */
    public void applyToEitherDemo() {
        String result = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            println("[in supplyAsync] hello //sleep 200ms");
            return "hello";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            sleep(100);
            println("[in applyToEither  - supplyAsync ] world //sleep 100ms");
            return "world";
        }), s -> s).join();
        System.out.println(result);
        sleep(200);
    }


























    /**
     * @param s
     */
    private void println(String s) {
        System.out.println(s);
    }


    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}