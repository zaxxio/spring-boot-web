package org.wsd.app.quartz;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Log4j2
public class SampleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobTimer jobTimer = (JobTimer) dataMap.get("1");

        if (jobTimer.isExpired()) {
            log.info("Job with id : {} has expired at execution time. Job will not run.", jobTimer.getJobId());
            return; // Exit the job without performing the task
        }

        log.info("Remaining Job Count : " + jobTimer.getRemainingCount());
    }
}
