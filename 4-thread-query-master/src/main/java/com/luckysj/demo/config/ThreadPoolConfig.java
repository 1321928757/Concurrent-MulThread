package com.luckysj.demo.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;

/**
 * @author Luckysj @刘仕杰
 * @description 线程池配置
 * @create 2024/03/19 21:43:57
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${thread.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${thread.pool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${thread.pool.queueCapacity}")
    private int queueCapacity;

    @Value("${thread.pool.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}