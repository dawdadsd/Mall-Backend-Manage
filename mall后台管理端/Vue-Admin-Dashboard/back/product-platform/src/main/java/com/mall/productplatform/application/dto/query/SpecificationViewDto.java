package com.mall.productplatform.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 规格视图DTO
 * 用于前端展示商品规格信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationViewDto {

    /**
     * 规格ID
     */
    private Long id;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 规格值列表
     */
    private List<SpecValueDto> values = new ArrayList<>();

    /**
     * 规格值DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecValueDto {
        
        /**
         * 规格值ID
         */
        private Long id;
        
        /**
         * 规格值
         */
        private String value;
    }
} 