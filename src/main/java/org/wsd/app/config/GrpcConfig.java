package org.wsd.app.config;

import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wsd.app.grpc.interceptor.ErrorHandlingClientInterceptor;

@Configuration
@ImportAutoConfiguration({
        net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration.class,
        net.devh.boot.grpc.client.autoconfigure.GrpcClientMetricAutoConfiguration.class,
        net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration.class,
        net.devh.boot.grpc.client.autoconfigure.GrpcClientSecurityAutoConfiguration.class,
        net.devh.boot.grpc.client.autoconfigure.GrpcClientMicrometerTraceAutoConfiguration.class,
        net.devh.boot.grpc.client.autoconfigure.GrpcDiscoveryClientAutoConfiguration.class,
        net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration.class,
})
public class GrpcConfig {


    @Autowired
    private ErrorHandlingClientInterceptor errorHandlingClientInterceptor;

    @Bean
    public GrpcChannelConfigurer clientInterceptorConfigurer() {
        return (channelBuilder, name) -> {
            if (channelBuilder instanceof ManagedChannelBuilder) {
                channelBuilder.intercept(errorHandlingClientInterceptor);
            }
        };
    }

}