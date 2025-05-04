package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 商品属性实体类
 * 用于存储商品的键值对属性
 */
@Entity
@Table(name = "product_attributes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    @Column(name = "sort")
    private Integer sort;
    
    @Column(name = "is_filterable")
    private Boolean isFilterable;
    
    @Column(name = "is_highlighted")
    private Boolean isHighlighted;
    
    /**
     * 设置为可过滤属性
     */
    public void setAsFilterable() {
        this.isFilterable = true;
    }
    
    /**
     * 设置为高亮属性
     */
    public void setAsHighlighted() {
        this.isHighlighted = true;
    }
} 