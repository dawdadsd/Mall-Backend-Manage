package com.mall.productplatform.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品SKU DTO
 * 用于传输商品SKU数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuDto {

    /**
     * SKU ID
     */
    private Long id;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * SKU价格
     */
    private BigDecimal price;

    /**
     * SKU库存
     */
    private Integer inventory;

    /**
     * SKU销量
     */
    private Integer sales;

    /**
     * SKU规格值映射
     * 键为规格名称，值为规格值
     */
    private Map<String, String> specifications = new HashMap<>();

    /**
     * SKU图片URL
     */
    private String imageUrl;

    /**
     * 是否可用
     */
    private Boolean enabled;
} 