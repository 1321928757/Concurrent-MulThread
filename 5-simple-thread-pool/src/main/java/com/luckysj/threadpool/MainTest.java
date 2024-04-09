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
    public static void main(String[] args){

        ThreadPool threadPool = new ThreadPool(new WorkQueue<>(5), 2, 5,5L, TimeUnit.SECONDS,
                (queue, task) -> {
                    log.info("拒绝策略====》拒绝策略触发，直接丢弃当前任务");
                }, new DefaultThreadFactory());

        threadPool.setAllowCoreThreadTimeOut(false);
        for (int i = 0; i < 15; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                log.info("执行任务{}------->当前执行线程为{}" , finalI, Thread.currentThread().toString());
            });
        }

        threadPool.shutdown();

    }
}
