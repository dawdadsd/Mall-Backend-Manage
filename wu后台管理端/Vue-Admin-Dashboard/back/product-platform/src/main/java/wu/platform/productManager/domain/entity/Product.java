package wu.platform.productManager.domain.entity;

import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@ToString(exclude = {"category", "merchant", "images", "attributes", "specifications", "skus"})
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    
    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;
    
    @Lob
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`", nullable = false, length = 20)
    private ProductCondition condition;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "name")
    @Builder.Default
    private Map<String, ProductAttribute> attributes = new HashMap<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Builder.Default
    private List<ProductSpecification> specifications = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Builder.Default
    private List<ProductSku> skus = new ArrayList<>();

    /**
     * 领域逻辑：添加商品图片
     */
    public void addImage(String imageUrl, int sortOrder) {
        ProductImage image = new ProductImage();
        image.setImageUrl(imageUrl);
        image.setSortOrder(sortOrder);
        image.setProduct(this);
        this.images.add(image);
    }
    
    /**
     * 领域逻辑：移除商品图片
     */
    public void removeImage(String imageUrl) {
        this.images.removeIf(img -> img.getImageUrl().equals(imageUrl));
    }
    
    /**
     * 领域逻辑：添加商品属性
     */
    public void addAttribute(String name, String value) {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setName(name);
        attribute.setValue(value);
        attribute.setProduct(this);
        this.attributes.put(name, attribute);
    }
    
    /**
     * 领域逻辑：移除商品属性
     */
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }
    
    /**
     * 领域逻辑：添加商品规格
     */
    public void addSpecification(ProductSpecification specification) {
        specification.setProduct(this);
        this.specifications.add(specification);
    }
    
    /**
     * 领域逻辑：移除商品规格
     */
    public void removeSpecification(String name) {
        this.specifications.removeIf(spec -> spec.getName().equals(name));
    }
    
    /**
     * 领域逻辑：添加SKU
     */
    public void addSku(ProductSku sku) {
        sku.setProduct(this);
        this.skus.add(sku);
    }
    
    /**
     * 领域逻辑：移除SKU
     */
    public void removeSku(String skuCode) {
        this.skus.removeIf(sku -> sku.getSkuCode().equals(skuCode));
    }

    public void updateStatus(ProductStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("状态不能为空");
        }
        // Add status transition validation logic if needed
        this.status = newStatus;
    }
} 