package com.mall.merchant.model;

import com.mall.merchant.model.enums.ViolationHandLingStatus;
import com.mall.merchant.model.enums.ViolationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 商家违规记录实体类
 * 记录商家的违规行为、处理状态和处理结果
 */
@Entity
@Table(name = "merchant_violations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationId;

    /**
     * 关联的商家
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @NotNull
    private Merchant merchant;

    /**
     * 违规类型：虚假描述/侵权/发货延迟/服务差等
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViolationType violationType;

    /**
     * 违规发生时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime violationDate;

    /**
     * 违规详细描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 证据链接
     */
    @Column(length = 500)
    private String evidenceUrl;

    /**
     * 处理状态：待处理/已处理/申诉中
     */
    @Enumerated(EnumType.STRING)
    private ViolationHandLingStatus handlingStatus = ViolationHandLingStatus.PENDING;

    /**
     * 处理结果：警告/降分/限制功能/禁用账号等
     */
    @Column(length = 200)
    private String handlingResult;

    /**
     * 处理人员ID
     */
    private Long handlerId;

    /**
     * 处理时间
     */
    private LocalDateTime handlingTime;

    /**
     * 对信用分的影响值
     */
    private Double creditScoreImpact;
} 