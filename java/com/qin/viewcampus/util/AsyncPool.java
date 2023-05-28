package com.qin.viewcampus.util;

import org.slf4j.helpers.MarkerIgnoringBase;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncPool {

    // 配置核心线程数
    private int corePoolSize = 5;
    // 配置最大线程数
    private int maxPoolSize = 20;
    // 配置任务队列的长度
    private int queueCapacity = 500;
    // 配置任务的空闲时间
    private int aliveSeconds = 600;
    // 配置线程前缀
    private String namePrefix = "localThreadPool";

    @Bean(name = "localBootAsyncExecutor")
    public Executor asyncServiceExecutor() {
        MarkerIgnoringBase log = null;
        log.info("初始化 springboot 线程池");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);
        //配置线程的空闲时间
        executor.setKeepAliveSeconds(aliveSeconds);

        // RejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        log.info("springboot 线程池初始化完毕");
        return executor;
    }
}
