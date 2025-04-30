package com.mall.productplatform.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建商品SKU命令
 * 用于接收创建商品SKU的请求数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductSkuCommand {

    /**
     * SKU编码
     */
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;

    /**
     * SKU价格
     */
    @NotNull(message = "SKU价格不能为空")
    @Positive(message = "SKU价格必须大于0")
    private BigDecimal price;

    /**
     * SKU库存
     */
    @NotNull(message = "SKU库存不能为空")
    @PositiveOrZero(message = "SKU库存不能小于0")
    private Integer inventory;

    /**
     * SKU规格值映射
     * 键为规格名称，值为规格值
     */
    private Map<String, String> specifications = new HashMap<>();

    /**
     * SKU图片URL
     */
    private String imageUrl;
} 