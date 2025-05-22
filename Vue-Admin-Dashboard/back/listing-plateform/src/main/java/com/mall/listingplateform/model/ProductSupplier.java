package com.mall.listingplateform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 产品供应商实体类
 * 用于存储和管理供应商信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_supplier")
@Builder
public class ProductSupplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String name;
    
    /**
     * 供应商编码
     */
    @NotBlank(message = "供应商编码不能为空")
    @Size(max = 30)
    @Column(nullable = false, unique = true)
    private String code;
    
    /**
     * 联系人
     */
    @Column(length = 20)
    private String contactPerson;
    
    /**
     * 联系电话
     */
    @Column(length = 20)
    private String contactPhone;
    
    /**
     * 联系邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Column(length = 50)
    private String contactEmail;
    
    /**
     * 供应商地址
     */
    @Column(length = 200)
    private String address;
    
    /**
     * 采购链接
     */
    @Column(name = "purchase_link", length = 255)
    private String purchaseLink;
    
    /**
     * 合作状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CooperationStatus status = CooperationStatus.ACTIVE;
    
    /**
     * 质量评级(1-5星)
     */
    @Column(name = "quality_rating")
    private Integer qualityRating;
    
    /**
     * 供应商描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * 关联的产品列表
     */
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
    
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
     * 合作状态枚举
     */
    public enum CooperationStatus {
        ACTIVE,         // 活跃
        INACTIVE,       // 不活跃
        SUSPENDED,      // 暂停
        TERMINATED      // 终止
    }
} 