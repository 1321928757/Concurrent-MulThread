package com.luckysj.threadpool.policy;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;

/**
 * @author Luckysj @刘仕杰
 * @description 拒绝策略接口
 * @create 2024/03/27 11:29:46
 */
public interface RejectPolicy<T> {
    void reject(ThreadPool pool, T task);

}
