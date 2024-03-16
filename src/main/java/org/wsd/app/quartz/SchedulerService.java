/*
 * Copyright (c) of Partha Sutradhar 2024.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.wsd.app.quartz;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final Scheduler scheduler;

    @PostConstruct
    public void start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public <T extends Job> void schedule(Class<T> clazz, ExecutionTimer executionTimer) {
        final JobDetail jobDetail = buildJobDetail(clazz, executionTimer);
        final Trigger trigger = buildTrigger(clazz, executionTimer);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }

    }

    private <T extends Job> Trigger buildTrigger(Class<T> clazz, ExecutionTimer executionTimer) {
        if (executionTimer.getCronExpression() != null && !StringUtils.isEmpty(executionTimer.getCronExpression())) {
            return TriggerBuilder
                    .newTrigger()
                    .withIdentity(clazz.getSimpleName() + executionTimer.getId())
                    .withSchedule(CronScheduleBuilder.cronSchedule(executionTimer.getCronExpression()))
                    .build();
        } else {
            SimpleScheduleBuilder builder = SimpleScheduleBuilder
                    .simpleSchedule()
                    .withIntervalInMilliseconds(executionTimer.getRepeatedInterval());

            if (executionTimer.isRunForever()) {
                builder = builder.repeatForever();
            } else {
                builder = builder.withRepeatCount(executionTimer.getMaxExecution() - 1);
            }

            return TriggerBuilder.newTrigger()
                    .withIdentity(clazz.getSimpleName() + executionTimer.getId())
                    .withSchedule(builder)
                    .startAt(new Date(System.currentTimeMillis() + executionTimer.getInitialDelay()))
                    .build();
        }
    }

    private <T extends Job> JobDetail buildJobDetail(Class<T> clazz, ExecutionTimer executionTimer) {
        final JobDataMap dataMap = new JobDataMap();
        dataMap.put("userId", executionTimer.getId());
        return JobBuilder
                .newJob(clazz)
                .withIdentity(clazz.getSimpleName())
                .setJobData(dataMap)
                .build();
    }

    @PreDestroy
    private void stop() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
