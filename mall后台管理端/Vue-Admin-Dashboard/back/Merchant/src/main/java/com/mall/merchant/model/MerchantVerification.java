package com.mall.merchant.model;

import com.mall.merchant.model.enums.CertificationType;
import com.mall.merchant.model.enums.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 商家身份认证申请实体类
 * 记录商家的认证申请、审核状态和审核结果
 */
@Entity
@Table(name = "merchant_verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    /**
     * 关联的商家
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @NotNull
    private Merchant merchant;

    /**
     * 认证申请类型：个人认证/企业认证
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationType verificationType;

    /**
     * 申请提交时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime applicationTime;

    /**
     * 上传的认证材料
     * 可以存储JSON格式的文件链接列表
     */
    @Column(length = 2000)
    private String materials;

    /**
     * 申请状态：审核中/已通过/已拒绝
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;

    /**
     * 申请处理时间
     */
    @UpdateTimestamp
    private LocalDateTime processingTime;

    /**
     * 处理结果说明（如拒绝原因）
     */
    @Column(length = 500)
    private String resultReason;

    /**
     * 处理人员ID
     */
    private Long processorId;
} 