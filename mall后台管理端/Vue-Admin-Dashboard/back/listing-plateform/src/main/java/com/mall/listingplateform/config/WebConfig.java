package com.mall.listingplateform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 处理CORS和其他Web相关配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置CORS规则
     * 允许前端应用跨域访问API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 1小时内不再预检
    }
} 