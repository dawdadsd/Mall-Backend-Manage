package com.mall.productplatform.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * JPA配置类
 * 配置JPA相关特性
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.mall.productplatform.infrastructure.repository.jpa")
public class JpaConfig {

    /**
     * 提供审计信息的Bean
     * 返回当前用户ID作为创建者和修改者
     * 
     * @return 审计信息提供者
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 这里可以从SecurityContext获取当前用户信息
        // 简化实现，返回系统用户ID
        return () -> Optional.of("SYSTEM");
    }
} 