package com.hrms.absence.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Allow API routes
                .allowedOrigins("http://localhost:4200") // Allow Angular app on this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific methods
                .allowCredentials(true);
    }
}
