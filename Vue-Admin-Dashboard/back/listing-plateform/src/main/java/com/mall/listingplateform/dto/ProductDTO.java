package com.mall.listingplateform.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank(message = "商品编码不能为空")
    private String code;

    private Long brandId;
    private String brandName;

    @NotNull(message = "商品分类不能为空")
    private Long categoryId;
    private String categoryName;

    private String description;
    private String detailContent;
    private String mainImage;
    private String images;
    private String video;

    private String status;
    private LocalDateTime publishTime;
    private LocalDateTime unpublishTime;

    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;
    private BigDecimal promotionPrice;

    private Integer stock;
    private Integer warningStock;

    private String keywords;
    private String searchTags;
    private String specifications;
    private String attributes;

    // 商家相关
    private Long merchantId;
    private String merchantName;
    private Integer merchantFansCount;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
