package org.wsd.app.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableFeignClients
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        var httpMessageConverters = new HttpMessageConverters(new MappingJackson2HttpMessageConverter(), new ByteArrayHttpMessageConverter(),
                new StringHttpMessageConverter(), new AllEncompassingFormHttpMessageConverter());
        return new ResponseEntityDecoder(new SpringDecoder(() -> httpMessageConverters, customizers));
    }

    @Bean
    public Encoder feignEncoder() {
        var httpMessageConverters = new HttpMessageConverters(new MappingJackson2HttpMessageConverter(), new ByteArrayHttpMessageConverter(),
                new StringHttpMessageConverter(), new AllEncompassingFormHttpMessageConverter());
        return new SpringEncoder(() -> httpMessageConverters);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
                String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
            }
        };
    }
}
