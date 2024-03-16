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

package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.quartz.ExecutionTimer;
import org.wsd.app.quartz.SchedulerService;
import org.wsd.app.quartz.TaskRunner;

@RestController
@Tag(name = "Scheduler Controller")
@RequiredArgsConstructor
@SecurityRequirement(name = "BEARER_TOKEN")
@RequestMapping("/api/schedulers")
public class SchedulerController {
    private final SchedulerService schedulerService;
    @PostMapping("/{id}")
    public void execute(@PathVariable("id") int id) {
        ExecutionTimer executionTimer = new ExecutionTimer();
        executionTimer.setId(id);
        executionTimer.setMaxExecution(5);
        executionTimer.setRepeatedInterval(5000);
//        executionTimer.setCronExpression("0/5 * * * * ? *");
        executionTimer.setRunForever(false);
        executionTimer.setInitialDelay(5000);
        executionTimer.setData("Execution Completed!!");
        schedulerService.schedule(TaskRunner.class, executionTimer);
    }
}
