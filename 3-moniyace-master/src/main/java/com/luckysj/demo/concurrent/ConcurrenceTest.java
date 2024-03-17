package com.luckysj.demo.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 牵着猫散步的鼠鼠 @刘仕杰
 * @description 多线程模拟高并发
 * @create 2024/03/17 21:09:44
 */
@Slf4j
public class ConcurrenceTest {
    public static void concurrenceTest() {
        /**
         * 模拟高并发情况代码
         */
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final CountDownLatch countDownLatch = new CountDownLatch(1000); // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
        final CountDownLatch countDownLatch2 = new CountDownLatch(1000); // 保证所有线程执行完了再打印atomicInteger的值
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            // 循环创建1000个任务，加入到线程池中
            for (int i = 0; i < 1000; i++) {
                executorService.submit(() -> {
                    try {
                        countDownLatch.await(); //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                    //每个线程执行1000次，每次加1
                    for (int j = 0; j < 1000; j++) {
                        // 这里用业务代码替换
                        // xxxxxx
                        atomicInteger.incrementAndGet();
                    }
                    countDownLatch2.countDown();
                });
                // 添加完成后倒计时器-1
                countDownLatch.countDown();
            }
            // 等待所有线程执行完毕
            countDownLatch2.await();
            executorService.shutdown();
            log.info("atomicInteger的值为:{}", atomicInteger.get());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        concurrenceTest();
    }
}
