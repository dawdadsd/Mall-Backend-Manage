package com.mall.listingplateform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 创建商品 DTO
 * 用于API接收创建商品请求的数据
 */
@Data
public class ProductCreateDTO {

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称不能超过100个字符")
    private String name; // 商品名称

    @Size(max = 50, message = "商品编码不能超过50个字符")
    private String code; // 商品编码 (可选)

    @NotBlank(message = "商品描述不能为空")
    @Size(max = 200, message = "商品描述不能超过200个字符")
    private String description; // 描述

    private String detailContent; // 详细内容 (HTML/Markdown)

    @NotNull(message = "必须指定商品分类")
    private Long categoryId; // 分类ID

    private Long brandId; // 品牌ID (可选)

    @NotNull(message = "必须关联商家")
    private Long merchantId; // !!! 关联的商家ID !!!

    private Long supplierId; // 供应商ID (可选)

    @NotNull(message = "销售价格不能为空")
    @Positive(message = "销售价格必须为正数")
    private BigDecimal sellingPrice; // 售价

    private BigDecimal originalPrice; // 原价 (可选)

    @NotNull(message = "库存不能为空")
    @Positive(message = "库存必须为正数") // 或 @Min(0) 如果允许0库存
    private Integer stock; // 库存

    @NotBlank(message = "必须提供商品主图")
    private String mainImage; // 主图 URL

    private List<String> images; // 其他图片 URL 列表
    private String video; // 视频 URL (可选)

    private Map<String, String> specifications; // 规格 (Key-Value)
    private Map<String, String> attributes; // 属性 (Key-Value)

    private List<String> keywords; // 搜索关键词
    private List<String> searchTags; // 搜索标签
} 