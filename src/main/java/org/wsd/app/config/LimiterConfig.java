package org.wsd.app.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Duration;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class LimiterConfig {
    public static final String PHOTO_SERVICE_API = "PHOTO-SERVICE-API";
    private final RateLimiterRegistry rateLimiterRegistry;

    @Bean
    public RateLimiter rateLimiterPhotoServiceApi() {
        final RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(20))
                .limitForPeriod(5)
                .timeoutDuration(Duration.ofSeconds(5))
                .build();
        return rateLimiterRegistry.rateLimiter(LimiterConfig.PHOTO_SERVICE_API, config);
    }

}
