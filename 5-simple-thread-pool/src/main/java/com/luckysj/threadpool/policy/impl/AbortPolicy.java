package com.luckysj.threadpool.policy.impl;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Luckysj @刘仕杰
 * @description 中止策略(JDK默认的拒绝策略)，直接抛出异常
 * @create 2024/04/09 10:34:38
 */
@Slf4j
public class AbortPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(ThreadPool pool, Runnable task) {
        log.info("拒绝策略触发==中止策略=={}", task);
        throw new RuntimeException("Task " + task.toString() + " rejected from " + pool);
    }
}
