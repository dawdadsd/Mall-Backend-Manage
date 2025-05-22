package com.mall.listingplateform.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 商品数据质量实体类
 * 用于存储和管理商品数据的质量信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_data_quality")
@Builder
public class ProductDataQuality {
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
     * 数据质量评分(0-100)
     */
    @Column(name = "quality_score")
    private Integer qualityScore;
    
    /**
     * 数据清洗状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "clean_status", nullable = false)
    private DataCleanStatus cleanStatus = DataCleanStatus.PENDING;
    
    /**
     * 最后清洗时间
     */
    @Column(name = "last_clean_time")
    private LocalDateTime lastCleanTime;
    
    /**
     * 数据问题计数
     */
    @Column(name = "problem_count")
    private Integer problemCount = 0;
    
    /**
     * 数据问题详情(JSON格式)
     */
    @Column(name = "problem_details", columnDefinition = "TEXT")
    private String problemDetails;
    
    /**
     * 数据来源
     */
    @Column(name = "data_source", length = 50)
    private String dataSource;
    
    /**
     * 源系统标识
     */
    @Column(name = "source_system", length = 50)
    private String sourceSystem;
    
    /**
     * 数据版本
     */
    @Column(name = "data_version", length = 20)
    private String dataVersion;
    
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
     * 数据清洗状态枚举
     */
    public enum DataCleanStatus {
        PENDING,        // 待清洗
        IN_PROGRESS,    // 清洗中
        COMPLETED,      // 已完成
        FAILED,         // 失败
        VERIFIED        // 已验证
    }
    
    /**
     * 数据质量级别计算
     * 根据质量评分返回对应的质量级别
     */
    @Transient  // 不映射到数据库字段
    public String getQualityLevel() {
        if (qualityScore == null) {
            return "未评估";
        }
        
        if (qualityScore >= 90) {
            return "优秀";
        } else if (qualityScore >= 75) {
            return "良好";
        } else if (qualityScore >= 60) {
            return "一般";
        } else {
            return "较差";
        }
    }
} 