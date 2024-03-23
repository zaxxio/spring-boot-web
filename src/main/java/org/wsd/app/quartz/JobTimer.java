package org.wsd.app.quartz;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobTimer implements Serializable {
    private String jobId;
    private String jobName;
    private String groupName;
    private String cronExpression;
    private int totalFireCount;
    private int remainingCount;
    private boolean runForever;
    private long repeatIntervalMs;
    private long initialOffsetMs;
    private String callback;
}
