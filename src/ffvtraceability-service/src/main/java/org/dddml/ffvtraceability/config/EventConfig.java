package org.dddml.ffvtraceability.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class EventConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${async.executor.core-pool-size:2}")
    private int corePoolSize;

    @Value("${async.executor.max-pool-size:3}")
    private int maxPoolSize;

    @Value("${async.executor.queue-capacity:2500}")
    private int queueCapacity;

//    @Value("${async.executor.task-delay-ms:200}")
//    private long taskDelayMs;

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        eventMulticaster.setErrorHandler(throwable -> {
            log.error("Error in event processing", throwable);
        });

        return eventMulticaster;
    }

    @Bean
    public Executor asyncEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncEvent-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
//        executor.setTaskDecorator(runnable -> () -> {
//            try {
//                Thread.sleep(taskDelayMs);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.error("Task delay interrupted", e);
//            }
//            runnable.run();
//        });
        
        executor.initialize();
        return executor;
    }
}
