package com.luckysj.threadpool.policy.impl;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Luckysj @刘仕杰
 * @description 丢弃策略，直接丢弃任务
 * @create 2024/04/09 10:40:16
 */
@Slf4j
public class DiscardPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(ThreadPool pool, Runnable task) {
        // Do nothing
        log.info("拒绝策略触发==丢弃策略=={}", task);
    }
}