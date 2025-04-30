package com.mall.productplatform.application.dto.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品规格DTO
 * 用于传输商品规格数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationDto {

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空")
    @Size(max = 50, message = "规格名称不能超过50个字符")
    private String name;

    /**
     * 规格值列表
     */
    private List<String> values = new ArrayList<>();
} 