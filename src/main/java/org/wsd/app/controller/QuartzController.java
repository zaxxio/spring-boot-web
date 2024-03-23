package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.quartz.JobTimer;
import org.wsd.app.quartz.QuartzSchedulerService;
import org.wsd.app.quartz.SampleJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Quartz Controller")
@SecurityRequirement(name = "BEARER_TOKEN")
@RequestMapping("/api/quartz")
@RequiredArgsConstructor
public class QuartzController {
    private final QuartzSchedulerService quartzSchedulerService;

    @GetMapping(path = "/configure")
    public boolean configure(@Valid JobTimer jobTimer) {
        this.quartzSchedulerService.scheduleJob(SampleJob.class, jobTimer);
        return true;
    }

    @GetMapping(path = "/getRunningTimers")
    public List<JobTimer> getRunningTimers() {
        return quartzSchedulerService.getRunningTimers();
    }

    @GetMapping(path = "/getRunningTimer")
    public JobTimer getRunningTimer(@RequestParam("timerId") String timerId, @RequestParam("group") String group) {
        return quartzSchedulerService.getRunningTimer(timerId, group);
    }
}
