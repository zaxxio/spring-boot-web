package org.wsd.app.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancerConfiguration {

    /*@Bean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withHints()
                //.withRetryAwareness()
                //.withZonePreference()
                .build(context);
    }*/

}
