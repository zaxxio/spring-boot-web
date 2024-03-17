package org.wsd.app.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Log4j2
@EnableAsync
@EnableRetry
@Configuration
@EnableCaching
@EnableScheduling
public class Config {

    @Bean
    @Primary
    public ApplicationEventMulticaster multicaster(ThreadPoolTaskExecutor executor,
                                                   @Qualifier("applicationEventListenerErrorHandler") ApplicationListenerErrorHandler errorHandler) {
        final SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(executor);
        multicaster.setErrorHandler(errorHandler);
        return multicaster;
    }

    @Primary
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Async-[Group-1]-");
        executor.setRejectedExecutionHandler(((r, threadPoolExecutor) -> log.info("Task rejected, thread pool is full and queue is also full.")));
        executor.initialize();
        return executor;
    }

    @Bean
    public ApplicationListenerErrorHandler applicationEventListenerErrorHandler() {
        return new ApplicationListenerErrorHandler();
    }

    public static class ApplicationListenerErrorHandler implements org.springframework.util.ErrorHandler {
        @Override
        public void handleError(Throwable t) {
            // Log the error or perform other error handling here
            log.error("Error in event listener: " + t.getMessage());
        }
    }

}
