package org.wsd.app.security.config;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.ExecutorService;

public class SecurityContextPropagator {

    public static void propagate(ExecutorService executor, Runnable task) {
        SecurityContext originalContext = SecurityContextHolder.getContext();
        executor.submit(() -> {
            try {
                SecurityContextHolder.setContext(originalContext);
                task.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
        });
    }
}