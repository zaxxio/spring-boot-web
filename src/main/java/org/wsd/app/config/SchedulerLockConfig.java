package org.wsd.app.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S")
public class SchedulerLockConfig {
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }


//    @Scheduled(fixedRateString = "15", timeUnit = TimeUnit.SECONDS)
//    @SchedulerLock(name = "RoutineScheduler.scheduledTask", lockAtLeastFor = "PT15S", lockAtMostFor = "PT30S")
//    public void scheduledTask() {
//        System.out.println("S2 Server Scheduler open: " + LocalDateTime.now());
//    }

}
