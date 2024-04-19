package com.luckysj.threadpool.core;

import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Luckysj @刘仕杰
 * @description 任务阻塞队列BlockQueue
 * @create 2024/03/25 19:36:14
 */
@Slf4j
public class WorkQueue<T> {
    // 双端队列
    private Deque<T> deque = new ArrayDeque<>();
    // 队列的容量
    private int size;
    // 可重入锁
    private ReentrantLock lock = new ReentrantLock();

    private Condition fullCondition = lock.newCondition();
    private Condition emptyCondition = lock.newCondition();

    public WorkQueue(int size) {
        this.size = size;
    }

    // 添加任务 阻塞添加
    public void put(T task) {
        // 1.上锁
        lock.lock();
        try {
            // 2.首先检查队列是否满了
            while (size == deque.size()) {
                // 满了，那么我就等吧
                fullCondition.await();
            }
            // 3.将任务存入队列中
            deque.addLast(task);
            log.info("等待队列====》任务添加成功");
            // 4.唤醒挂起的消费者
            emptyCondition.signal();
        } catch (InterruptedException e) {
            // sleep等等状态下的线程如果被中断会抛出InterruptedException，我们要手动恢复中断状态
            Thread.currentThread().interrupt();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 获取任务 阻塞获取
    public T take() throws InterruptedException {
        // 1.上锁
        lock.lock();
        try {
            // 2.首先检查队列是否存在元素
            while (deque.isEmpty()) {
                // 空的，那我也等吧
                emptyCondition.await();
            }
            // 3.拿取元素
            T task = deque.removeFirst();
            log.info("等待队列====》线程{}任务拿取成功", Thread.currentThread());
            // 4.唤醒挂起的生产者
            fullCondition.signal();
            return task;
        }  finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 非堵塞添加任务
    public Boolean offer(T task)  {
        // 1.上锁
        lock.lock();
        try {
            if (size == deque.size()) {
                return false;
            }
            // 3.将任务存入队列中
            deque.addLast(task);
            log.info("等待队列====》任务添加成功");
            // 4.唤醒挂起的消费者
            emptyCondition.signal();
            return true;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 带超时时间阻塞添加
    public Boolean offer(T task, Long timeout, TimeUnit unit) {
        // 1.上锁
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout); // 转为毫秒
            // 2.首先检查队列是否满了
            while (size == deque.size()) {
                try {
                    // 2.1超时判断，返回值是剩余时间
                    if (nanos <= 0) {
                        return false;
                    }
                    // 2.2超时等待
                    log.debug("等待队列====》任务等待加入任务队列 {} ...", task);
                    nanos = fullCondition.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    // sleep等等状态下的线程如果被中断会抛出InterruptedException，我们要手动恢复中断状态
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            // 3.将任务存入队列中
            deque.addLast(task);
            log.info("等待队列====》任务添加成功");
            // 4.唤醒挂起的消费者
            emptyCondition.signal();
            return true;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 非阻塞获取
    public T poll(){
        // 1.上锁
        lock.lock();
        try {
            // 2.首先检查队列是否存在元素
            T task = null;
            if(!deque.isEmpty()){

                task = deque.removeFirst();
                fullCondition.signal();
            }
            return task;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
    // 带超时时间阻塞获取
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        // 1.上锁
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout); // 转为毫秒
            // 2.首先检查队列是否存在元素
            while (deque.isEmpty()) {
                    // 2.1超时判断，返回值是剩余时间
                    if (nanos <= 0) {
                        return null;
                    }
                    // 2.2超时等待
                    log.debug("等待队列====》{}线程等待获取任务", Thread.currentThread());
                    nanos = emptyCondition.awaitNanos(nanos);
            }
            // 3.拿取元素
            T task = deque.removeFirst();
            log.info("等待队列====》线程{}任务拿取成功", Thread.currentThread());
            // 4.唤醒挂起的生产者
            fullCondition.signal();
            return task;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }

}
