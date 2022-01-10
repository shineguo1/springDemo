package gxj.study.leetcode.unknown;

import java.util.concurrent.atomic.AtomicInteger;

class FooBar {
    private int n;
    private AtomicInteger gateway = new AtomicInteger(0);

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; ) {
            while(gateway.intValue() == 0){
                synchronized(FooBar.class){
                    if(gateway.intValue() == 0){
                        printFoo.run();
                        gateway.set(1);
                    }
                }
            }
            // printFoo.run() outputs "foo". Do not change or remove this line.
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            while(gateway.intValue() == 1){
                synchronized(FooBar.class){
                    if(gateway.intValue() == 1){
                        printBar.run();
                        gateway.set(0);
                    }
                }
            }

        }
    }
}