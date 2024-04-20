package com.luckysj.threadpool;

import com.luckysj.threadpool.core.ThreadPool;
import com.luckysj.threadpool.core.WorkQueue;
import com.luckysj.threadpool.factory.DefaultThreadFactory;
import com.luckysj.threadpool.policy.impl.CallerRunsPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@Slf4j
public class MainTest {
    public static void main(String[] args){

        // 1.开启线程池
        ThreadPool threadPool = new ThreadPool(new WorkQueue<>(5), 2, 5,5L, TimeUnit.SECONDS,
                new CallerRunsPolicy(), new DefaultThreadFactory());

       // 2.允许核心线程回收
        threadPool.setAllowCoreThreadTimeOut(false);
        for (int i = 0; i < 15; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                log.info("执行任务{}------->当前执行线程为{}" , finalI, Thread.currentThread().toString());
            });
        }

        // 3.尝试关闭线程池
        threadPool.shutdown();
    }

    private Node addWaiter(Node mode) {
        //创建 Node 类，并且设置 thread 为当前线程，设置为排它锁
        Node node = new Node(Thread.currentThread(), mode);
        // 获取 AQS 中队列的尾部节点
        Node pred = tail;
        // 如果 tail == null，说明是空队列，
        // 不为 null，说明现在队列中有数据，
        if (pred != null) {
            // 将当前节点的 prev 指向刚才的尾部节点，那么当前节点应该设置为尾部节点
            node.prev = pred;
            // CAS 将 tail 节点设置为当前节点
            if (compareAndSetTail(pred, node)) {
                // 将之前尾节点的 next 设置为当前节点
                pred.next = node;
                // 返回当前节点
                return node;
            }
        }
        enq(node);
        return node;
    }

}
