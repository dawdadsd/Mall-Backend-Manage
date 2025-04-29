package com.mall.merchant.model;

import com.mall.merchant.model.enums.MerchantStatus;
import com.mall.merchant.model.enums.MerchantType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家实体类 - 仅包含基础商家信息
 * 其他扩展信息如认证信息、信用评级等放在独立的实体类中
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchant {
    /**
     * 商家ID - 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchantId;
    
    /**
     * 商家名称
     */
    @NotBlank
    @Column(nullable = false)
    private String merchantName;
    
    /**
     * 商家头像URL
     */
    private String avatarUrl;
    
    /**
     * 商家类型 - 品牌商家/认证个人/普通个人
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantType merchantType = MerchantType.REGULAR_INDIVIDUAL;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Email
    private String contactEmail;

    /**
     * 注册时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationTime;
    
    /**
     * 商家状态 - 活跃/禁用/冻结
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantStatus status = MerchantStatus.ACTIVE;
    
    /**
     * 最后更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updateTime;
    
    /**
     * 商家描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 商家等级ID - 外键
     * 通过MerchantLevel实体类关联商家等级信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private MerchantLevel merchantLevel;
    
    // 关联商家认证信息 - 一对一关系
    @OneToOne(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private MerchantCertification certification;
    
    // 关联商家信用评级 - 一对一关系
    @OneToOne(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private MerchantCreditRating creditRating;
    
    // 关联商家违规记录 - 一对多关系
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MerchantViolation> violations = new ArrayList<>();
    
    // 关联商家认证申请 - 一对多关系
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MerchantVerification> verificationRequests = new ArrayList<>();
    
    // 关联商家经营数据 - 一对一关系
    @OneToOne(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private MerchantBusinessData businessData;
}
