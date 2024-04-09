package com.luckysj.threadpool;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.factory.DefaultThreadFactory;
import com.luckysj.threadpool.policy.impl.CallerRunsPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainTest {
    public static void main(String[] args) {

        // 1.开启线程池
        ThreadPool threadPool = new ThreadPool(new WorkQueue<>(5), 2, 5,5L, TimeUnit.SECONDS,
                new CallerRunsPolicy(), new DefaultThreadFactory());


        // 2.允许核心线程回收
        threadPool.setAllowCoreThreadTimeOut(false);

        // 3.执行任务
        for (int i = 0; i < 15; i++) {
            threadPool.execute(() -> {
                log.info("执行任务------->当前执行线程为" + Thread.currentThread().toString());
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
