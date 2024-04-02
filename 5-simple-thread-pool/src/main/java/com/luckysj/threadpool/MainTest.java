package com.luckysj.threadpool;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.factory.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainTest {
    public static void main(String[] args) {



        ThreadPool threadPool = new ThreadPool(new WorkQueue<>(5), 2, 5,5L, TimeUnit.SECONDS,
                (queue, task) -> {
                    System.out.println();
                }, new DefaultThreadFactory());

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                System.out.println("执行任务------->当前执行线程为" + Thread.currentThread().toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // ExecutorService executorService = Executors.newFixedThreadPool(2);

    }
}
