package com.mall.listingplateform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
/**
 * 后台管理端_商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(min = 3 , max = 20)
    @Column( nullable = false)
    //hibernate
    private String name;

    @NotBlank(message = "商品编码不能为空")
    @Size(max = 50,message = "商品编码不能大于50个字符")
    @Column(unique = true,nullable = false)
    private String code;

    @Column(name = "brand_id")
    private Long brandId;

    @NotNull(message = "商品分类不能为空")
    @Column(name = "category_id",nullable = false)
    private Long categoryId;

    @Size(min = 10,max = 100,message = "商品描述最短10个字符，最长不能超过100字符")
    @Column(length = 100)
    private String description;

    @Column(name = "detail_content",columnDefinition = "TEXT")
    private String detailContent;

    @Column(name = "main_image")
    private String mainImage;
    /**
     * 商品图片的URL集合(JSON数组格式存储)
     */
    @Column(columnDefinition = "TEXT")
    private String images;

    private String video;

    /**
     * 商品状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.DRAFT;
    /**
     * 上架时间
     */
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    /**
     * 下架时间
     */
    @Column(name = "unpublish_time")
    private LocalDateTime unpublishTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time",nullable = false)
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time",nullable = false,updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 价格相关字段
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    @Column(name = "selling_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellingPrice;
    
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;
    
    @Column(name = "promotion_price", precision = 10, scale = 2)
    private BigDecimal promotionPrice;
    
    /**
     * 库存信息
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;
    
    @Column(name = "warning_stock")
    private Integer warningStock;
    
    /**
     * SEO和搜索优化
     */
    @Column(length = 200)
    private String keywords;
    
    @Column(name = "search_tags", columnDefinition = "TEXT")
    private String searchTags;
    
    /**
     * 规格参数(JSON格式)
     */
    @Column(name = "specifications", columnDefinition = "TEXT")
    private String specifications;
    
    /**
     * 商品属性(JSON格式)
     */
    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;
    
    /**
     * 数据清洗关联
     */
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductDataQuality dataQuality;
    
    /**
     * 供应商信息关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private ProductSupplier supplier;
    
    /**
     * 商品分析数据关联
     */
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductAnalytics analytics;
    
    /**
     * 商品状态枚举
     */
    public enum Status {
        DRAFT,          // 草稿
        PENDING,        // 待审核
        APPROVED,       // 已审核
        PUBLISHED,      // 已上架
        UNPUBLISHED,    // 已下架
        DELETED         // 已删除
    }
}
