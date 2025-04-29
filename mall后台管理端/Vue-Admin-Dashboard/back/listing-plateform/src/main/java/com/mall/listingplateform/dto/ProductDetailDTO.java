package com.mall.listingplateform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品详细信息 DTO
 * 用于展示商品详情页，包含商家基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {

    private Long id; // 商品ID
    private String name; // 商品名称
    private String code; // 商品编码
    private String description; // 描述
    private String detailContent; // 详细内容 (HTML/Markdown)
    private List<String> images; // 商品图片列表
    private String mainImage; // 主图
    private String video; // 视频链接
    private BigDecimal sellingPrice; // 售价
    private BigDecimal originalPrice; // 原价
    private Integer stock; // 库存
    private String status; // 商品状态 (建议使用枚举或包含枚举信息的内部类)
    private Map<String, String> specifications; // 规格 (需要从数据库 text 解析)
    private Map<String, String> attributes; // 属性 (需要从数据库 text 解析)
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // --- 关联信息 --- (根据需要转换为对应的DTO)
    private Long categoryId; // 分类ID
    private Long brandId; // 品牌ID
    private Long supplierId; // 供应商ID (可以考虑 SupplierBriefDTO)

    // --- 商家信息 --- (从 Merchant 模块获取)
    private Long merchantId;
    private String merchantName;
    private String merchantAvatarUrl;
    private String merchantType; // 使用 String 或引入 MerchantType 枚举 (需处理依赖)
    private String merchantRatingLevel; // 使用 String 或引入 RatingLevel 枚举 (需处理依赖)

    // 或者使用嵌套 DTO (需要处理模块间依赖)
    // private com.mall.merchant.dto.MerchantBasicInfoDTO merchantInfo;
} 