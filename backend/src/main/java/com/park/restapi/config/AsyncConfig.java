package com.park.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {

    private static final int EMAIL_CORE_POOL_SIZE = 10;
    private static final int EMAIL_MAX_POOL_SIZE = 30;
    private static final int EMAIL_QUEUE_CAPACITY = 20;

    private static final int SLACK_CORE_POOL_SIZE = 20;
    private static final int SLACK_MAX_POOL_SIZE = 50;
    private static final int SLACK_QUEUE_CAPACITY = 100;

    @Bean
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(EMAIL_CORE_POOL_SIZE);
        executor.setMaxPoolSize(EMAIL_MAX_POOL_SIZE);
        executor.setQueueCapacity(EMAIL_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("Email-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor slackTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(SLACK_CORE_POOL_SIZE);
        executor.setMaxPoolSize(SLACK_MAX_POOL_SIZE);
        executor.setQueueCapacity(SLACK_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("Slack-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
