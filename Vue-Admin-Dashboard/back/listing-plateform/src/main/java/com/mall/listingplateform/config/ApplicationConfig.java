package com.mall.listingplateform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 应用配置类
 * 
 * 提供应用程序的配置，包括Bean注册、跨域配置等
 * 
 * @since 2025
 * @version 1.0
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Jackson ObjectMapper配置
     * 
     * @return 配置好的ObjectMapper实例
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    /**
     * 跨域配置
     * 
     * @return WebMvcConfigurer实现
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
} 