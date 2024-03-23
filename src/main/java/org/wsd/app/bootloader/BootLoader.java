package org.wsd.app.bootloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wsd.app.quartz.JobTimer;
import org.wsd.app.quartz.QuartzSchedulerService;
import org.wsd.app.quartz.SampleJob;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootLoader implements CommandLineRunner {
    private final QuartzSchedulerService quartzSchedulerService;

    @Override
    public void run(String... args) throws Exception {

    }
}
