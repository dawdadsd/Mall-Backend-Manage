package com.mall.productplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 规格值实体类
 * 存储如红色、XL等具体规格值
 */
@Entity
@Table(name = "specification_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", nullable = false)
    private ProductSpecification specification;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    @Column(name = "display_value")
    private String displayValue;
    
    @Column(name = "sort")
    private Integer sort;
    
    @Column(name = "color_code")
    private String colorCode;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    /**
     * 检查是否是颜色规格值
     */
    public boolean isColorValue() {
        return colorCode != null && !colorCode.isEmpty();
    }
    
    /**
     * 检查是否有自定义显示图片
     */
    public boolean hasImage() {
        return imageUrl != null && !imageUrl.isEmpty();
    }
    
    /**
     * 获取显示值（如果有设置）或者原始值
     */
    public String getDisplayOrValue() {
        return displayValue != null && !displayValue.isEmpty() ? displayValue : value;
    }
} 