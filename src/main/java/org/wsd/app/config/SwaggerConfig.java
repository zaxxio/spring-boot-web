package org.wsd.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:swagger/swagger.properties")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .description("Spring Boot App")
                        .contact(new Contact()
                                .name("Partha Sutradhar")
                                .email("partharaj.dev@gmail.com")
                                .url("https://linkedin.com/in/partha-sutradhar")
                        )
                        .license(new License().name("MIT"))
                        .title("Spring Boot App")
                        .version("1.0"));
    }


}
