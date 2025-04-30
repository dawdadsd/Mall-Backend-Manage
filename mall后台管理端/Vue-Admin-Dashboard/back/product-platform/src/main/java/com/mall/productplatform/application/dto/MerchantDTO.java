package com.mall.productplatform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商家DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO implements Serializable {
    
    private Long id;
    private String name;
    private String code;
    private String contactPerson;
    private String contactPhone;
    private String email;
    private String address;
    private String logoUrl;
    private String description;
    private Integer followers;
    private Double rating;
    private Boolean isVerified;
    private Boolean isEnabled;
    
    /**
     * 简化版DTO - 只包含基本信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MerchantBasicDTO implements Serializable {
        private Long id;
        private String name;
        private String code;
        private String logoUrl;
        private Double rating;
        private Boolean isVerified;
    }
} 