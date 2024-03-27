package com.luckysj.threadpool.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainTest {
    public static void main(String[] args) {

        ThreadPool threadPool = new ThreadPool(new WorkQueue<>(5), 2, 5L, TimeUnit.SECONDS,
                (queue, task) -> {
                    // 一直等
                    //queue.put(task);
                    // 调用者线程执行
                    //task.run();
                    // 直接抛出异常
                    // throw new RuntimeException("saa");
                    // 丢弃这个任务
                    log.debug("丢弃这个任务{}", task);
                });

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

    }
}
