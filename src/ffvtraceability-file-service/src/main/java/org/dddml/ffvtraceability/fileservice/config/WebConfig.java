package org.dddml.ffvtraceability.fileservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.mvc.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${spring.mvc.cors.allowed-methods:*}")
    private String allowedMethods;

    @Value("${spring.mvc.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${spring.mvc.cors.exposed-headers:*}")
    private String exposedHeaders;

    @Value("${spring.mvc.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${spring.mvc.cors.max-age:1800}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .exposedHeaders(exposedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
} 