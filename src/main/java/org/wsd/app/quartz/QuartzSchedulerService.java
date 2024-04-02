package org.wsd.app.quartz;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
        if (jobTimer.isExpired()) {
            log.info("Job with id : {} is expired and will not be scheduled.", jobTimer.getJobId());
            return; // Skip scheduling the job
        }
        try {
            scheduler.scheduleJob(job, trigger);
            log.info("Quartz job has been scheduled with id : " + jobTimer.getJobId());
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean startNow(String jobId) {
        JobKey jobKey = new JobKey(jobId);
        try {
            if (!scheduler.checkExists(jobKey)) {
                log.error("Job with ID {} does not exist", jobId);
                return false;
            }
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            Trigger instantTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobId)
                    .forJob(jobDetail)
                    .startNow()
                    .build();

            scheduler.scheduleJob(instantTrigger);
            log.info("Scheduled job {} to start instantly", jobId);
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to schedule job {} for immediate execution. Error: {}", jobId, e.getMessage(), e);
            return false;
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
        final JobKey jobKey = new JobKey(timerId);
        try {
            final JobDetail job = scheduler.getJobDetail(jobKey);
            job.getJobDataMap().put(jobKey.getName(), jobTimer);
            scheduler.addJob(job, true, true);
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
        if (StringUtils.isNotEmpty(jobTimer.getCronExpression())) {
            return TriggerBuilder.newTrigger()
                    .withIdentity(jobTimer.getJobId())
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTimer.getCronExpression()))
                    .startAt(new Date(System.currentTimeMillis() + jobTimer.getInitialOffsetMs()))
                    .build();
        } else {
            SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(jobTimer.getRepeatIntervalMs());

            if (jobTimer.isRunForever()) {
                builder = builder.repeatForever();
            } else {
                builder = builder.withRepeatCount(jobTimer.getTotalFireCount() - 1);
            }

            return TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobTimer.getJobId())
                    // .withIdentity(jobTimer.getJobId(), jobTimer.getGroupName())
                    .withSchedule(builder)
                    .startAt(new Date(System.currentTimeMillis() + jobTimer.getInitialOffsetMs()))
                    .build();
        }
    }

    private JobDetail buildJob(Class<? extends Job> clazz, JobTimer jobTimer) {
        final JobDataMap dataMap = new JobDataMap();
        dataMap.put(jobTimer.getJobId(), jobTimer);
        return JobBuilder
                .newJob(clazz)
                .withIdentity(jobTimer.getJobId())
                //.withIdentity(jobTimer.getJobId(), jobTimer.getGroupName())
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

    public boolean deleteJob(String jobId) {
        try {
            boolean deletedJob = scheduler.deleteJob(new JobKey(jobId));
            log.info("Quartz job has been deleted with id : " + jobId);
            return deletedJob;
        } catch (SchedulerException e) {
            return false;
        }
    }

    public boolean pauseJob(String jobId) {
        try {
            scheduler.pauseJob(new JobKey(jobId));
            log.info("Quartz job has been pause with id : " + jobId);
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }

    public boolean resumeJob(String jobId) {
        try {
            log.info("Quartz job has been resumed with id : " + jobId);
            scheduler.resumeJob(new JobKey(jobId));
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }
}
