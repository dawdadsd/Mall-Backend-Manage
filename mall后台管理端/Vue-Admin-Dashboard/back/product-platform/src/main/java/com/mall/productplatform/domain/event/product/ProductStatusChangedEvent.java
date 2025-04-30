package com.mall.productplatform.domain.event.product;

import com.mall.productplatform.domain.event.DomainEvent;
import com.mall.productplatform.domain.vo.ProductStatus;
import lombok.Getter;

/**
 * 商品状态变更事件
 * 当商品状态发生变化时触发，如上架、下架等
 */
@Getter
public class ProductStatusChangedEvent extends DomainEvent {
    
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
     * 先前状态
     */
    private final ProductStatus previousStatus;
    
    /**
     * 当前状态
     */
    private final ProductStatus currentStatus;

    /**
     * 构造商品状态变更事件
     * 
     * @param productId 商品ID
     * @param productName 商品名称
     * @param previousStatus 先前状态
     * @param currentStatus 当前状态
     */
    public ProductStatusChangedEvent(Long productId, String productName, 
                                  ProductStatus previousStatus, ProductStatus currentStatus) {
        super(CURRENT_VERSION);
        this.productId = productId;
        this.productName = productName;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }
    
    /**
     * 判断是否上架事件
     * 
     * @return 是否为上架事件
     */
    public boolean isOnSaleEvent() {
        return this.currentStatus == ProductStatus.ON_SALE;
    }
    
    /**
     * 判断是否下架事件
     * 
     * @return 是否为下架事件
     */
    public boolean isOffShelfEvent() {
        return this.currentStatus == ProductStatus.OFF_SHELF;
    }
    
    /**
     * 判断是否售罄事件
     * 
     * @return 是否为售罄事件
     */
    public boolean isSoldOutEvent() {
        return this.currentStatus == ProductStatus.SOLD_OUT;
    }
} 