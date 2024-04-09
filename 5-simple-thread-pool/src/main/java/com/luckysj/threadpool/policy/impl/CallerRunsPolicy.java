package com.luckysj.threadpool.policy.impl;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Luckysj @刘仕杰
 * @description 调用者执行策略
 * @create 2024/04/09 10:43:42
 */
@Slf4j
public class CallerRunsPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(ThreadPool pool, Runnable task) {
        if (!Thread.currentThread().isInterrupted()) {
            log.info("拒绝策略触发==调用者执行策略=={}", task);
            task.run();
        }
    }
}