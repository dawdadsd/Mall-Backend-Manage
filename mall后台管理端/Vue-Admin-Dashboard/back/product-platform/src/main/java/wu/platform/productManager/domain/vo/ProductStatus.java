package com.mall.productplatform.domain.vo;

import lombok.Getter;

/**
 * 商品状态枚举
 * 使用Java现代化设计，带有丰富的业务含义
 */
@Getter
public enum ProductStatus {
    ON_SALE("在售", "商品正在销售中，顾客可以购买"),
    OFF_SHELF("下架", "商品已经从销售列表中移除，顾客无法购买"),
    PENDING("待审核", "商品正在审核中，尚未上架销售"),
    SOLD_OUT("已售罄", "商品库存为零，暂时无法购买"),
    DELETED("已删除","平台审核删除了商品，不可购买");
    
    private final String displayName;
    private final String description;
    
    ProductStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * 判断是否可以被购买
     */
    public boolean isBuyable() {
        return this == ON_SALE;
    }
    
    /**
     * 判断是否可以上架
     */
    public boolean canBePutOnSale() {
        return this == OFF_SHELF || this == PENDING;
    }
} 