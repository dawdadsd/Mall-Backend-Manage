package com.mall.productplatform.domain.event.product;

import com.mall.productplatform.domain.event.DomainEvent;
import lombok.Getter;

/**
 * 商品创建事件
 * 当新商品被创建时触发
 */
@Getter
public class ProductCreatedEvent extends DomainEvent {
    
    /**
     * 当前事件版本
     */
    private static final int CURRENT_VERSION = 1;
    
    /**
     * 商品ID
     */
    private final Long productId;
    
    /**
     * 商品名称
     */
    private final String productName;
    
    /**
     * 商品分类ID
     */
    private final Long categoryId;
    
    /**
     * 商家ID
     */
    private final Long merchantId;

    /**
     * 构造商品创建事件
     * 
     * @param productId 商品ID
     * @param productName 商品名称
     * @param categoryId 分类ID
     * @param merchantId 商家ID
     */
    public ProductCreatedEvent(Long productId, String productName, Long categoryId, Long merchantId) {
        super(CURRENT_VERSION);
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.merchantId = merchantId;
    }
} 