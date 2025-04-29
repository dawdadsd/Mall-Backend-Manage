package com.mall.merchant.model;

import com.mall.merchant.model.enums.RatingLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 商家信用评级实体类
 * 存储商家的信用评分、评级级别和各项评价指标
 */
@Entity
@Table(name = "merchant_credit_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCreditRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    /**
     * 关联的商家
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @NotNull
    private Merchant merchant;

    /**
     * 信用评分：1.0-5.0的分值
     */
    @Column(precision = 3, scale = 2)  // 例如 4.75
    private Double creditScore = 5.0;   // 默认为5.0

    /**
     * 评级级别：优秀/良好/一般/较差
     */
    @Enumerated(EnumType.STRING)
    private RatingLevel ratingLevel = RatingLevel.EXCELLENT;

    /**
     * 交易履约率
     */
    @Column(precision = 5, scale = 4)  // 例如 0.9876 表示 98.76%
    private Double fulfillmentRate;

    /**
     * 准时发货率
     */
    @Column(precision = 5, scale = 4)
    private Double onTimeShippingRate;

    /**
     * 售后满意度
     */
    @Column(precision = 3, scale = 2)  // 例如 4.85
    private Double afterSalesSatisfaction;

    /**
     * 信用评级更新时间
     */
    @UpdateTimestamp
    private LocalDateTime lastUpdateTime;

    /**
     * 交易数据样本量
     */
    private Integer sampleSize;

    /**
     * 评分算法版本
     */
    private String algorithmVersion;
} 