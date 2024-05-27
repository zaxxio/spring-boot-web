package org.wsd.app.bootloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.wsd.app.domain.HighPerformanceEntity;
import org.wsd.app.quartz.JobTimer;
import org.wsd.app.quartz.QuartzSchedulerService;
import org.wsd.app.quartz.SampleJob;
import org.wsd.app.repository.HighPerformanceRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Long.sum;

@Component
@RequiredArgsConstructor
public class BootLoader implements CommandLineRunner {
    private final QuartzSchedulerService quartzSchedulerService;
    private final HighPerformanceRepository highPerformanceRepository;

    @Override
    public void run(String... args) throws Exception {


        Instant start = Instant.now();

        long sum = 0;
        sum = calculateSumWithPaginatedParallelProcessing(10000);

        System.out.println("Sum : " + sum);

        Instant end = Instant.now();
        System.out.println("Data : " + Duration.between(start, end).toMillis());


    }

    private long calculateSumWithPaginatedParallelProcessing(int pageSize) throws Exception {
        long totalSum = 0;
        long totalEntities = highPerformanceRepository.count();
        int totalPages = (int) Math.ceil((double) totalEntities / pageSize);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Double>> futures = new ArrayList<>();

        for (int page = 0; page < totalPages; page++) {
            int currentPage = page;
            Future<Double> future = executorService.submit(() -> {
                double sum = 0;
                Page<HighPerformanceEntity> entityPage = highPerformanceRepository.findAll(PageRequest.of(currentPage, pageSize));
                sum = entityPage.get().parallel()
                        .mapToDouble(HighPerformanceEntity::getData)
                        .sum();
                return sum;
            });
            futures.add(future);
        }

        for (Future<Double> future : futures) {
            totalSum += future.get();
        }

        executorService.shutdown();
        return totalSum;
    }
}
