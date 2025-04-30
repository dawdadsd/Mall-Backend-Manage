package com.mall.productplatform.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家DTO
 * 用于传输商家信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {

    /**
     * 商家ID
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家标识
     */
    private String code;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 商家状态
     */
    private String status;
} 