package org.wsd.app.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class SampleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobTimer jobTimer = (JobTimer) dataMap.get("SampleJob");
        System.out.println("Executing job..." + new Date() + " " + jobTimer);
    }
}
