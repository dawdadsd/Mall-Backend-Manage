package com.mall.listingplateform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Jackson ObjectMapper配置
     * 
     * @return 配置好的ObjectMapper实例
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        // 使用 builder 来获取 Spring Boot 默认的 ObjectMapper 配置，避免完全覆盖
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // Register JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Add custom serializers/deserializers for LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

        objectMapper.registerModule(javaTimeModule);

        // Optional: Configure not to write dates as timestamps
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
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
                        .allowedOrigins("*") // 生产环境建议指定具体的源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(false) // 通常API不需要凭证
                        .maxAge(3600);
            }
        };
    }
} 