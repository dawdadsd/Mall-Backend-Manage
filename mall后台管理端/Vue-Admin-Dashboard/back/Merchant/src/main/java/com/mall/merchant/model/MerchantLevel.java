package com.mall.merchant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家等级实体类
 * 用于定义商家的不同等级及其对应的权益和要求
 */
@Entity
@Table(name = "merchant_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;

    /**
     * 等级名称 - 如"钻石"、"金牌"等
     */
    @NotBlank
    @Column(unique = true, nullable = false)
    private String levelName;

    /**
     * 该等级的最低信用分要求
     */
    @NotNull
    private Double minCreditScore;

    /**
     * 平台抽成比例
     */
    @NotNull
    @Column(precision = 5, scale = 4)  // 例如 0.0500 表示 5%
    private BigDecimal commissionRate;

    /**
     * 等级权益描述
     */
    @Column(length = 1000)
    private String benefitsDescription;

    /**
     * 可发布商品数量上限
     */
    private Integer productLimit;

    /**
     * 特殊功能列表
     */
    @Column(length = 500)
    private String specialFeatures;

    /**
     * 关联的商家列表
     */
    @OneToMany(mappedBy = "merchantLevel")
    private List<Merchant> merchants = new ArrayList<>();
} 