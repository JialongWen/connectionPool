package com.wjl.connectionpool.test;

public class Test {
    public static void main(String[] args) {
        for (int i = 1; i < 4; i++) {
            TestThread testThread = new TestThread();
            Thread thread = new Thread(testThread,"线程："+i);
            thread.start();
        }
    }
}
