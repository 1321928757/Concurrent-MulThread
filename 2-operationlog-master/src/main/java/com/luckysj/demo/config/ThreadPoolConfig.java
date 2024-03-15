package com.luckysj.demo.config;

import com.luckysj.demo.utils.ThreadUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    /**
     * 线程池配置
     */
    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    /**
     * 创建线程池
     *
     * @return 线程池
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        // 最大可创建的线程数
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        // 等待队列最大长度
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        // 线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(threadPoolProperties.getCorePoolSize(),
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                ThreadUtils.printException(r, t);
            }
        };
    }

}
