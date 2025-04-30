package com.mall.productplatform.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SKU库存更新DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuInventoryUpdateDTO implements Serializable {
    
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;
    
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer inventory;
} 