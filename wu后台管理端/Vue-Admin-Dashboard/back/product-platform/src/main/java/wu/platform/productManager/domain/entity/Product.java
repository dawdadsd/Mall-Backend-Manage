package wu.platform.productManager.domain.entity;

import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品实体类 - 领域模型的核心聚合根
 * 采用DDD设计理念，聚合了商品的各个方面
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
    
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "inventory", nullable = false)
    private Integer inventory;
    
    @Column(name = "sales")
    private Integer sales;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private ProductCondition condition;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductImage> images = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductAttribute> attributes = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductSpecification> specifications = new HashSet<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductSku> skus = new HashSet<>();

    /**
     * 领域逻辑：添加商品图片
     */
    public Product addImage(ProductImage image) {
        this.images.add(image);
        image.setProduct(this);
        return this;
    }
    
    /**
     * 领域逻辑：移除商品图片
     */
    public Product removeImage(ProductImage image) {
        this.images.remove(image);
        image.setProduct(null);
        return this;
    }
    
    /**
     * 领域逻辑：添加商品属性
     */
    public Product addAttribute(ProductAttribute attribute) {
        this.attributes.add(attribute);
        attribute.setProduct(this);
        return this;
    }
    
    /**
     * 领域逻辑：添加商品规格
     */
    public Product addSpecification(ProductSpecification specification) {
        this.specifications.add(specification);
        specification.setProduct(this);
        return this;
    }
    
    /**
     * 领域逻辑：添加SKU
     */
    public Product addSku(ProductSku sku) {
        this.skus.add(sku);
        sku.setProduct(this);
        return this;
    }
    
    /**
     * 领域逻辑：上架商品
     */
    public void putOnSale() {
        if (this.inventory <= 0) {
            throw new IllegalStateException("库存容量小于0，不能上架");
        }
        this.status = ProductStatus.ON_SALE;
    }
    
    /**
     * 领域逻辑：下架商品
     */
    public void takeOffSale() {
        this.status = ProductStatus.OFF_SHELF;
    }
    
    /**
     * 领域逻辑：更新库存
     */
    public void updateInventory(int newInventory) {
        if (newInventory < 0) {
            throw new IllegalArgumentException("库存不能设置为小于0");
        }
        this.inventory = newInventory;
        // 自动更新商品状态
        if (newInventory == 0 && this.status == ProductStatus.ON_SALE) {
            this.status = ProductStatus.SOLD_OUT;
        } else if (newInventory > 0 && this.status == ProductStatus.SOLD_OUT) {
            this.status = ProductStatus.ON_SALE;
        }
    }
    
    /**
     * 领域逻辑：记录销售
     */
    public void recordSale(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("销售商品的数量不可能小于0");
        }
        if (this.inventory < quantity) {
            throw new IllegalStateException("库存不足");
        }
        this.inventory -= quantity;
        this.sales = (this.sales != null ? this.sales : 0) + quantity;
        // 自动更新状态
        if (this.inventory == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }
} 