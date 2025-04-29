package com.mall.merchant.model;

import com.mall.merchant.model.enums.CertificationStatus;
import com.mall.merchant.model.enums.CertificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商家认证信息实体类
 * 存储商家的认证状态、类型和认证材料等信息
 */
@Entity
@Table(name = "merchant_certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificationId;

    /**
     * 关联的商家
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @NotNull
    private Merchant merchant;

    /**
     * 认证状态：已认证/认证中/未认证/认证失败
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationStatus status = CertificationStatus.UNVERIFIED;

    /**
     * 认证时间
     */
    private LocalDateTime certificationTime;

    /**
     * 认证类型：个人认证/企业认证
     */
    @Enumerated(EnumType.STRING)
    private CertificationType certificationType;

    /**
     * 认证材料：身份证/营业执照等文件链接
     * 可以存储JSON格式的文件链接列表
     */
    @Column(length = 2000)
    private String certificationMaterials;

    /**
     * 处理认证的管理员ID
     */
    private Long auditorId;

    /**
     * 认证失败原因
     */
    @Column(length = 500)
    private String failureReason;
} 