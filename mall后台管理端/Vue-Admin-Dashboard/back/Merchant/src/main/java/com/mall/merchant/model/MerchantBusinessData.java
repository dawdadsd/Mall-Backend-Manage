package com.mall.merchant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家经营数据实体类
 * 存储商家的销售额、订单数量等经营数据
 */
@Entity
@Table(name = "merchant_business_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantBusinessData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessDataId;

    /**
     * 关联的商家
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @NotNull
    private Merchant merchant;

    /**
     * 总销售额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal totalSales = BigDecimal.ZERO;

    /**
     * 月销售额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal monthlySales = BigDecimal.ZERO;

    /**
     * 订单总数
     */
    private Integer totalOrders = 0;

    /**
     * 平均订单金额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal averageOrderValue = BigDecimal.ZERO;

    /**
     * 退款率
     */
    @Column(precision = 5, scale = 4)  // 例如 0.0350 表示 3.5%
    private Double refundRate = 0.0;

    /**
     * 商品数量
     */
    private Integer productCount = 0;

    /**
     * 最后更新时间
     */
    @UpdateTimestamp
    private LocalDateTime lastUpdateTime;

    /**
     * 统计数据开始时间
     */
    private LocalDateTime statisticsStartDate;
} 