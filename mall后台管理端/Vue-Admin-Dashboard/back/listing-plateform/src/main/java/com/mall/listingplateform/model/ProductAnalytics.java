package com.mall.listingplateform.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 商品分析数据实体类
 * 用于存储和管理商品的分析数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_analytics")
@Builder
public class ProductAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联的商品
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    /**
     * 销量
     */
    @Column(name = "sales_volume")
    @Builder.Default
    private Integer salesVolume = 0;
    
    /**
     * 浏览量
     */
    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;
    
    /**
     * 收藏数
     */
    @Column(name = "favorite_count")
    @Builder.Default
    private Integer favoriteCount = 0;
    
    /**
     * 评论数
     */
    @Column(name = "comment_count")
    @Builder.Default
    private Integer commentCount = 0;
    
    /**
     * 平均评分(1-5)
     */
    @Column(name = "average_rating", precision = 3, scale = 1)
    private BigDecimal averageRating;
    
    /**
     * 转化率(%)
     */
    @Column(name = "conversion_rate", precision = 5, scale = 2)
    private BigDecimal conversionRate;
    
    /**
     * 退货率(%)
     */
    @Column(name = "return_rate", precision = 5, scale = 2)
    private BigDecimal returnRate;
    
    /**
     * AI优化建议(JSON格式)
     */
    @Column(name = "ai_suggestions", columnDefinition = "TEXT")
    private String aiSuggestions;
    
    /**
     * 竞品价格范围
     */
    @Column(name = "competitor_price_range", length = 50)
    private String competitorPriceRange;
    
    /**
     * 市场趋势(1-5，1=下降，3=稳定，5=上升)
     */
    @Column(name = "market_trend")
    private Integer marketTrend;
    
    /**
     * 季节性指数(0-100)
     */
    @Column(name = "seasonality_index")
    private Integer seasonalityIndex;
    
    /**
     * 最后分析时间
     */
    @Column(name = "last_analyzed_time")
    private LocalDateTime lastAnalyzedTime;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
    
    /**
     * 获取商品热度等级
     * 基于浏览量、收藏数和销量计算
     */
    @Transient
    public String getPopularityLevel() {
        int score = 0;
        
        // 根据浏览量计分
        if (viewCount >= 10000) score += 5;
        else if (viewCount >= 5000) score += 4;
        else if (viewCount >= 1000) score += 3;
        else if (viewCount >= 500) score += 2;
        else if (viewCount >= 100) score += 1;
        
        // 根据收藏数计分
        if (favoriteCount >= 1000) score += 5;
        else if (favoriteCount >= 500) score += 4;
        else if (favoriteCount >= 100) score += 3;
        else if (favoriteCount >= 50) score += 2;
        else if (favoriteCount >= 10) score += 1;
        
        // 根据销量计分
        if (salesVolume >= 500) score += 5;
        else if (salesVolume >= 200) score += 4;
        else if (salesVolume >= 100) score += 3;
        else if (salesVolume >= 50) score += 2;
        else if (salesVolume >= 10) score += 1;
        
        // 根据总分返回热度等级
        if (score >= 12) return "爆款";
        else if (score >= 9) return "热销";
        else if (score >= 6) return "畅销";
        else if (score >= 3) return "一般";
        else return "冷门";
    }
} 