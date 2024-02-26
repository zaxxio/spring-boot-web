package org.wsd.app.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class BulkheadConfiguration {
    public static final String PHOTO_SERVICE_API = "photoServiceApi";
    private final BulkheadRegistry bulkheadRegistry;

    @Bean
    public Bulkhead photoServiceApiBulkhead() {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(500))
                .build();
        return bulkheadRegistry.bulkhead(PHOTO_SERVICE_API, config);
    }
}
