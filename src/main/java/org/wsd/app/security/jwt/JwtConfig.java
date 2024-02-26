package org.wsd.app.security.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Data
@Configuration
@NoArgsConstructor
@PropertySource("classpath:jwt/jwt.properties")
@ConfigurationProperties(prefix = "auth")
public class JwtConfig {
    private String secret;
    private int expiration;
}

