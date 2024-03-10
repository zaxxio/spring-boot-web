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

package org.wsd.app.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
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
public class ResilienceConfig {
    public static final String AUTH_SERVICE_API = "AUTH-SERVICE-API";

    private final RateLimiterRegistry rateLimiterRegistry;
    private final BulkheadRegistry bulkheadRegistry;

    @Bean
    public Bulkhead photoServiceApiBulkhead() {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(500))
                .build();
        return bulkheadRegistry.bulkhead(AUTH_SERVICE_API, config);
    }

    @Bean
    public RateLimiter rateLimiterPhotoServiceApi() {
        final RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(5)
                .timeoutDuration(Duration.ofSeconds(5))
                .build();
        return rateLimiterRegistry.rateLimiter(ResilienceConfig.AUTH_SERVICE_API, config);
    }

}
