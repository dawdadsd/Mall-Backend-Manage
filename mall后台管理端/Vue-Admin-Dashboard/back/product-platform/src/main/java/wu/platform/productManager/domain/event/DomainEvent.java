package com.mall.productplatform.domain.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * 领域事件基类
 * 所有领域事件继承自此类
 */
@Getter
public abstract class DomainEvent {
    
    /**
     * 事件唯一标识
     */
    private final String eventId;
    
    /**
     * 事件发生时间
     */
    private final Instant occurredOn;
    
    /**
     * 事件版本，用于处理事件版本兼容性
     */
    private final int version;

    /**
     * 构造函数
     * @param version 事件版本号
     */
    protected DomainEvent(int version) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.version = version;
    }
    
    /**
     * 获取事件类型
     * @return 事件类型
     */
    public String getType() {
        return this.getClass().getSimpleName();
    }
} 