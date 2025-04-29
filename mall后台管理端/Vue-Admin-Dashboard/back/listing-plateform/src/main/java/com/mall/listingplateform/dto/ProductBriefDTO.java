package com.mall.listingplateform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品简略信息 DTO
 * 用于在商家管理页面展示其商品列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBriefDTO {

    private Long id;
    private String name;
    private String mainImage;
    private BigDecimal sellingPrice;
    private String status; // 商品状态 (使用 String 或对应的 Enum DTO)
    private Integer stock; // 库存
    private LocalDateTime createTime; // 创建时间
}
