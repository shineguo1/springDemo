package gxj.study.demo.juc;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by xinjie_guo on 2019/12/2.
 */
public class CompletableFutureDemo1Test {
    @Test
    public void runAsyncDemo() throws Exception {
        new CompletableFutureDemo1().runAsyncDemo();
    }

    @Test
    public void supplyAsyncDemo() throws Exception {
        new CompletableFutureDemo1().supplyAsyncDemo();
    }

    @Test
    public void completeDemo1() throws ExecutionException, InterruptedException {
        new CompletableFutureDemo1().completeDemo1();
    }

    @Test
    public void completeDemo2() throws ExecutionException, InterruptedException {
        new CompletableFutureDemo1().completeDemo2();
    }

    @Test
    public void completeDemo3() throws ExecutionException, InterruptedException {
        new CompletableFutureDemo1().completeDemo3();
    }

    @Test
    public void thenApplyDemo1() throws ExecutionException, InterruptedException {
        new CompletableFutureDemo1().thenApplyDemo();
    }

    @Test
    public void thenApplyDemo2() throws ExecutionException, InterruptedException {
        new CompletableFutureDemo1().thenApplyAsyncDemo();
    }

    @Test
    public void thenAccept(){
        new CompletableFutureDemo1().thenAcceptDemo();
    }

    @Test
    public void thenRunAsync(){
        new CompletableFutureDemo1().thenRunAsync();
    }

    @Test
    public void thenCombineDemo(){
        new CompletableFutureDemo1().thenCombineDemo();
    }

    @Test
    public void thenAcceptBothDemo(){
        new CompletableFutureDemo1().thenAcceptBothDemo();
    }

    @Test
    public void applyToEitherDemo(){
        new CompletableFutureDemo1().applyToEitherDemo();
    }


}