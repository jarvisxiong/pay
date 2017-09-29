package com.senyint.test.netty;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.Future;

import java.util.concurrent.ExecutionException;

public class EventExecutorTest {

    public static void main(String[] args) throws InterruptedException {

        DefaultEventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(1);
        eventExecutors.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("a point");
            }
        });

        Thread.sleep(100);
        eventExecutors.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("b point");
                throw new RuntimeException("runtime err");
            }
        });

        Thread.sleep(10000);
        Future future = eventExecutors.shutdownGracefully();
        if (future.isSuccess()) {
            try {
                Object val = future.get();
                System.out.println(val);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

}