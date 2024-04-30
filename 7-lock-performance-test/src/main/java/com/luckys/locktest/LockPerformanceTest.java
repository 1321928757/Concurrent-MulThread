package com.luckysj.leetcode;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockPerformanceTest {

    private static final int READ_THREADS = 10; //读操作线程数
    private static final int WRITE_THREADS = 2; //写操作线程数
    private static final int ITERATIONS = 100000; // 操作次数

    private static final Lock reentrantLock = new ReentrantLock();
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = readWriteLock.readLock();
    private static final Lock writeLock = readWriteLock.writeLock();

    private static int sharedResource = 0;

    public static void main(String[] args) throws InterruptedException {
        long startTime, endTime;

        // 测试 ReentrantLock
        startTime = System.currentTimeMillis();
        testReentrantLock();
        endTime = System.currentTimeMillis();
        System.out.println("ReentrantLock time: " + (endTime - startTime) + " ms");

        // 重置共享资源
        sharedResource = 0;

        // 测试 ReentrantReadWriteLock
        startTime = System.currentTimeMillis();
        testReentrantReadWriteLock();
        endTime = System.currentTimeMillis();
        System.out.println("ReentrantReadWriteLock time: " + (endTime - startTime) + " ms");
    }

    private static void testReentrantLock() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(READ_THREADS + WRITE_THREADS);

        for (int i = 0; i < READ_THREADS; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < ITERATIONS; j++) {
                        reentrantLock.lock();
                        // 读取共享资源
                        int value = sharedResource;
                        reentrantLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        for (int i = 0; i < WRITE_THREADS; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < ITERATIONS; j++) {
                        reentrantLock.lock();
                        // 写入共享资源
                        sharedResource++;
                        reentrantLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void testReentrantReadWriteLock() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(READ_THREADS + WRITE_THREADS);

        for (int i = 0; i < READ_THREADS; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < ITERATIONS; j++) {
                        readLock.lock();
                        // 读取共享资源
                        int value = sharedResource;
                        readLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        for (int i = 0; i < WRITE_THREADS; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < ITERATIONS; j++) {
                        writeLock.lock();
                        // 写入共享资源
                        sharedResource++;
                        writeLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
