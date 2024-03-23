package org.wsd.app.quartz.listener;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.wsd.app.quartz.JobTimer;
import org.wsd.app.quartz.QuartzSchedulerService;

@Service
@RequiredArgsConstructor
public class SimpleTriggerListener implements TriggerListener {
    private final QuartzSchedulerService quartzSchedulerService;

    @Override
    public String getName() {
        return SimpleTriggerListener.class.getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        final String timerId = trigger.getKey().getName();
        final JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobTimer jobTimer = (JobTimer) dataMap.get(timerId);
        if (!jobTimer.isRunForever()) {
            int remainingCount = jobTimer.getRemainingCount();
            if (!StringUtils.isNotEmpty(jobTimer.getCronExpression())) {
                jobTimer.setRemainingCount(remainingCount - 1);
            }
        }
        this.quartzSchedulerService.updateTimer(timerId, null, jobTimer);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {

    }
}
