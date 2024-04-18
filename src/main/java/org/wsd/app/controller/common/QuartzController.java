/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.quartz.JobTimer;
import org.wsd.app.quartz.QuartzSchedulerService;
import org.wsd.app.quartz.SampleJob;

import java.util.List;

@RestController
@Tag(name = "Quartz Controller")
@SecurityRequirement(name = "BEARER_TOKEN")
@RequestMapping("/api/quartz")
@RequiredArgsConstructor
public class QuartzController {
    private final QuartzSchedulerService quartzSchedulerService;

    @Operation(description = "Configure and schedule a Job.", summary = "Endpoint for configuring new scheduled Job.")
    @PostMapping(path = "/configure")
    public boolean configure(@Valid JobTimer jobTimer) {
        this.quartzSchedulerService.scheduleJob(SampleJob.class, jobTimer);
        return true;
    }

    @Operation(description = "Running Jobs.", summary = "Endpoint for find all running jobs.")
    @GetMapping(path = "/getRunningTimers")
    public List<JobTimer> getRunningTimers() {
        return quartzSchedulerService.getRunningTimers();
    }

    @Operation(description = "Delete Jobs.", summary = "Endpoint for deleting jobs.")
    @DeleteMapping(path = "/deleteJob")
    public boolean deleteJob(@RequestParam("jobId") String jobId) {
        return quartzSchedulerService.deleteJob(jobId);
    }

    @Operation(description = "Pause Jobs.", summary = "Endpoint for pausing jobs.")
    @PutMapping(path = "/pauseJob")
    public boolean pauseJob(@RequestParam("jobId") String jobId) {
        return quartzSchedulerService.pauseJob(jobId);
    }

    // need more work on this
    @Operation(description = "Start Job", summary = "Endpoint for starting existing instantly.")
    @PostMapping(path = "/startNow")
    public boolean startNow(@RequestParam("jobId") String jobId) {
        return quartzSchedulerService.startNow(jobId);
    }

    @Operation(description = "Resume Job", summary = "Endpoint for resuming existing job.")
    @PutMapping(path = "/resumeJob")
    public boolean resumeJob(@RequestParam("jobId") String jobId) {
        return quartzSchedulerService.resumeJob(jobId);
    }

    @Operation(description = "Find running Job", summary = "Endpoint for finding existing job.")
    @GetMapping(path = "/getRunningTimer")
    public JobTimer getRunningTimer(@RequestParam("timerId") String timerId, @RequestParam("group") String group) {
        return quartzSchedulerService.getRunningTimer(timerId, group);
    }
}
