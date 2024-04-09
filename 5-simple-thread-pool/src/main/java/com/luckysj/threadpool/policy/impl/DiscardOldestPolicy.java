package com.luckysj.threadpool.policy.impl;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Luckysj @刘仕杰
 * @description 丢弃旧任务策略
 * @create 2024/04/09 10:41:12
 */
@Slf4j
public class DiscardOldestPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(ThreadPool pool, Runnable task) {
        log.info("拒绝策略触发==丢弃旧任务策略=={}", task);
        pool.getQueue().poll();
        pool.execute(task);
    }
}