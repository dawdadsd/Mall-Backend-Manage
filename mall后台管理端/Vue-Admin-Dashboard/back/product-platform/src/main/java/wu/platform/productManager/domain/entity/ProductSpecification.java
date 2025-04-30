package com.mall.productplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * 商品规格实体类
 * 如颜色、尺寸等规格维度
 */
@Entity
@Table(name = "product_specifications")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    @Column(name = "sort")
    private Integer sort;
    
    @Column(name = "is_required")
    private Boolean isRequired;
    
    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderBy("sort ASC")
    private Set<SpecificationValue> values = new HashSet<>();
    
    /**
     * 添加规格值
     */
    public ProductSpecification addValue(SpecificationValue value) {
        values.add(value);
        value.setSpecification(this);
        return this;
    }
    
    /**
     * 移除规格值
     */
    public ProductSpecification removeValue(SpecificationValue value) {
        values.remove(value);
        value.setSpecification(null);
        return this;
    }
    
    /**
     * 设置为必选规格
     */
    public void setAsRequired() {
        this.isRequired = true;
    }
    
    /**
     * 设置值
     * @param value 规格值
     */
    public void setValues(String value) {
        this.value = value;
    }
}