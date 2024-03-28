package com.luckysj.threadpool.core;

import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Luckysj @刘仕杰
 * @description 自定义线程池对象
 * @create 2024/03/27 10:45:17
 */
@Slf4j
public class ThreadPool {
    /** 任务等待队列 */
    private WorkQueue<Runnable> workQueue;
    /** 正在运行的工作线程集合 */
    private final Set<Worker> workerSet = new HashSet<>();
    /** 核心线程数 */
    private int corePoolSize;
    /** 最大等待时间（也就是线程的最大空闲时间) */
    private Long keepAliveTime;
    /** 等待时间单位 */
    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(WorkQueue<Runnable> workQueue, int corePoolSize, Long keepAliveTime, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.workQueue = workQueue;
        this.corePoolSize = corePoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    // 为了方便使用，我们封装工作线程
    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            log.info("工作线程{}开始运行", Thread.currentThread());

            // 1。首先消费当前任务，消费完再去任务队列取，while循环实现线程复用
            while(task != null || (task = workQueue.poll(keepAliveTime, timeUnit)) != null){
                try {
                    task.run();
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    // 执行完后清除任务
                    task = null;
                }
            }

            // 2.跳出循环，说明取任务超过了最大等待时间，线程歇菜休息吧
            synchronized (workerSet){
                workerSet.remove(this);
            }
            log.info("线程{}超过最大空闲时间没有获取到任务，已被回收", Thread.currentThread());

        }
    }

    public void execute(Runnable task){
        synchronized(workerSet){
            //1 判断当前运行的工作线程数是否小于核心线程数
            if(workerSet.size() <  corePoolSize){
                // 2.1 创建工作线程
                Worker worker = new Worker(task);
                // 2.2 加入运行线程集合
                workerSet.add(worker);
                // 2.3 运行线程
                worker.start();

            }else{
                // 2.1 尝试将任务加入阻塞队列中等待，如果加入失败，触发拒绝策略
                workQueue.tryPut(rejectPolicy, task);

            }
        }
    }
}
