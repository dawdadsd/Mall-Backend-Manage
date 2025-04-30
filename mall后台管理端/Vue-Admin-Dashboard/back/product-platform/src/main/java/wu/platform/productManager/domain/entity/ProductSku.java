package com.mall.productplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品SKU实体类
 * 表示具体的库存单元
 */
@Entity
@Table(name = "product_skus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSku extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "sku_code", nullable = false, unique = true)
    private String skuCode;
    
    @Column(name = "specifications_json", columnDefinition = "TEXT")
    private String specificationsJson;
    
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    
    @Column(name = "original_price", precision = 19, scale = 2)
    private BigDecimal originalPrice;
    
    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;
    
    @Column(name = "inventory", nullable = false)
    private Integer inventory;
    
    @Column(name = "sales")
    private Integer sales;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;
    
    @Column(name = "volume", precision = 10, scale = 2)
    private BigDecimal volume;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    
    @ManyToMany
    @JoinTable(
        name = "sku_specification_values",
        joinColumns = @JoinColumn(name = "sku_id"),
        inverseJoinColumns = @JoinColumn(name = "value_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SpecificationValue> specificationValues = new HashSet<>();
    
    /**
     * 添加规格值
     */
    public ProductSku addSpecificationValue(SpecificationValue value) {
        specificationValues.add(value);
        return this;
    }
    
    /**
     * 移除规格值
     */
    public ProductSku removeSpecificationValue(SpecificationValue value) {
        specificationValues.remove(value);
        return this;
    }
    
    /**
     * 更新库存
     */
    public void updateInventory(int newInventory) {
        if (newInventory < 0) {
            throw new IllegalArgumentException("库存不能为负");
        }
        this.inventory = newInventory;
    }
    
    /**
     * 记录销售
     */
    public void recordSale(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("销售数量不能小于0");
        }
        
        if (this.inventory < quantity) {
            throw new IllegalStateException("销售数量不能大于当前库存");
        }
        
        this.inventory -= quantity;
        this.sales = (this.sales != null ? this.sales : 0) + quantity;
    }
    
    /**
     * 检查是否有库存
     */
    public boolean hasStock() {
        return this.inventory > 0;
    }
    
    /**
     * 检查是否低库存警告
     */
    public boolean isLowStock(int threshold) {
        return this.inventory > 0 && this.inventory <= threshold;
    }
    
    /**
     * 减少库存
     */
    public void decreaseInventory(int quantity) {
        if (inventory < quantity) {
            throw new IllegalStateException("库存不足");
        }
        this.inventory -= quantity;
    }
    
    /**
     * 增加库存
     */
    public void increaseInventory(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("增加的库存数量必须大于0");
        }
        this.inventory += quantity;
    }
    
    /**
     * 启用SKU
     */
    public void enable() {
        this.isEnabled = true;
    }
    
    /**
     * 禁用SKU
     */
    public void disable() {
        this.isEnabled = false;
    }
}