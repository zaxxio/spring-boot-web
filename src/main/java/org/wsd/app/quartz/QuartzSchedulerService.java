package org.wsd.app.quartz;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.wsd.app.quartz.listener.SimpleTriggerListener;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuartzSchedulerService {
    private final Scheduler scheduler;

    @PostConstruct
    public void onInit() {
        try {
            scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }


    public void scheduleJob(Class<? extends Job> clazz, JobTimer jobTimer) {
        final JobDetail job = buildJob(clazz, jobTimer);
        final Trigger trigger = buildTrigger(jobTimer);

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public JobTimer getRunningTimer(String timerId, String group) {
        final JobKey jobKey = new JobKey(timerId, group);
        try {
            final JobDetail job = scheduler.getJobDetail(jobKey);
            return (JobTimer) job.getJobDataMap().get(jobKey.getName());
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    public void updateTimer(String timerId, String group, JobTimer jobTimer) {
        final JobKey jobKey = new JobKey(timerId, group);
        try {
            final JobDetail job = scheduler.getJobDetail(jobKey);
            job.getJobDataMap().put(jobKey.getName(), jobTimer);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<JobTimer> getRunningTimers() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        log.info("Job Key " + jobKey);
                        try {
                            final JobDetail job = scheduler.getJobDetail(jobKey);
                            return (JobTimer) job.getJobDataMap().get(jobKey.getName());
                        } catch (SchedulerException ex) {
                            log.info(ex.getMessage(), ex);
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .toList();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private Trigger buildTrigger(JobTimer jobTimer) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(jobTimer.getRepeatIntervalMs());

        if (jobTimer.isRunForever()) {
            builder = builder.repeatForever();
        } else {
            builder = builder.withRepeatCount(jobTimer.getTotalFireCount() - 1);
        }

        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobTimer.getJobId(), jobTimer.getGroupName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + jobTimer.getInitialOffsetMs()))
                .build();
    }

    private JobDetail buildJob(Class<? extends Job> clazz, JobTimer jobTimer) {
        final JobDataMap dataMap = new JobDataMap();
        dataMap.put(jobTimer.getJobId(), jobTimer);
        return JobBuilder
                .newJob(clazz)
                .withIdentity(jobTimer.getJobId(), jobTimer.getGroupName())
                .setJobData(dataMap)
                .build();
    }


    @PreDestroy
    public void onDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}
