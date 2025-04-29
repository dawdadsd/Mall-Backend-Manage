package com.mall.listingplateform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品查询参数
 * 用于接收前端传递的查询条件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryParams {
    
    /**
     * 商品名称，支持模糊查询
     */
    private String name;
    
    /**
     * 商品分类
     */
    private String category;
    
    /**
     * 商品状态
     */
    private String status;
    
    /**
     * 最低商家粉丝数
     */
    private Integer minFollowers;
    
    /**
     * 最高商家粉丝数
     */
    private Integer maxFollowers;
    
    /**
     * 商家ID
     */
    private Long merchantId;
    
    /**
     * 最低价格
     */
    private Double minPrice;
    
    /**
     * 最高价格
     */
    private Double maxPrice;
}
