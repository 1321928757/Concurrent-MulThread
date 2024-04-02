package com.luckysj.threadpool.factory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Luckysj @刘仕杰
 * @description 默认线程工厂，我们这里仿照源码写法，为每个线程分配线程组(默认会自动分配)，并为每个线程组
 * @create 2024/03/28 21:27:10
 */
public class DefaultThreadFactory implements ThreadFactory{
    /** 原子序号类，我们可以通过该类为线程工厂来获取一个随机序号，主要是为了区分不同线程池实例*/
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    /** 线程组，每个线程都需要属于一个线程组(平常使用未指定线程组会默认分配)*/
    private final ThreadGroup group;
    /** 原子序号类，我们可以通过该类为每个线程来获取一个随机序号*/
    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    /** 线程名前缀，以便于在日志、监控等场景下识别和管理线程。*/
    private final String namePrefix;

    public DefaultThreadFactory() {
        // 获取管理安全策略的类，通过这个类我们可以获取对应名称的线程组，SecurityManager 和 group 的存在是为了更好地控制线程的安全性和权限
        SecurityManager s = System.getSecurityManager();
        // 存在 SecurityManager实例,则通过 s.getThreadGroup() 获取一个受限制的线程组。
        // 如果不存在 SecurityManager 实例,则使用当前线程所在的线程组 Thread.currentThread().getThreadGroup()。
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        // 生成前缀
        this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        // 将线程设置为用户线程
        if(thread.isDaemon()){
            thread.setDaemon(false);
        }
        // 为线程设置默认优先级
        if(thread.getPriority() != Thread.NORM_PRIORITY){
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
