package com.alfonso.hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves the ReDoc page at a clean /redoc path. Swagger UI needs no equivalent
 * because the springdoc starter registers /swagger-ui.html itself.
 */
@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/redoc").setViewName("forward:/redoc.html");
    }
}
