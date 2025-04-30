package com.mall.productplatform.application.dto;

import com.mall.productplatform.domain.vo.ProductStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量状态更新DTO
 * 用于批量更新商品状态
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchStatusUpdateDTO implements Serializable {
    
    @NotEmpty(message = "商品ID列表不能为空")
    private List<Long> productIds;
    
    @NotNull(message = "商品状态不能为空")
    private ProductStatus status;
} 