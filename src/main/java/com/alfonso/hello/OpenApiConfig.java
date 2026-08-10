package com.alfonso.hello;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI apiDocs() {
        return new OpenAPI().info(new Info()
                .title("spring-hello-world")
                .version("0.0.1")
                .description("""
                        A minimal Spring Boot web app on Maven.

                        Interactive docs: [Swagger UI](/swagger-ui.html) · [ReDoc](/redoc)
                        """)
                .license(new License().name("MIT")));
    }
}
