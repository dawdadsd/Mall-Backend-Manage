package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU实体
 * 代表商品的最小可销售单元
 */
@Entity
@Table(name = "product_skus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product", "specifications"})
public class ProductSku extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku_code", nullable = false, unique = true, length = 50)
    private String skuCode;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "inventory", nullable = false)
    private Integer inventory = 0;

    @Column(name = "sales")
    private Integer sales = 0;

    @Column(name = "image_url")
    private String imageUrl;

    /**
     * SKU规格组合
     * 例如: {"颜色": "红色", "尺寸": "L"}
     * 使用 JSONB 或类似类型存储，或者使用关联表
     * 这里用 ElementCollection 简化处理
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_sku_specifications", joinColumns = @JoinColumn(name = "sku_id"))
    @MapKeyColumn(name = "spec_name", length = 50)
    @Column(name = "spec_value", length = 100)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Map<String, String> specifications;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    // Business methods

    public void increaseInventory(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("增加的库存数量不能为负数");
        }
        this.inventory += quantity;
    }

    public void decreaseInventory(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("减少的库存数量不能为负数");
        }
        if (this.inventory < quantity) {
            throw new IllegalStateException("库存不足，无法扣减");
        }
        this.inventory -= quantity;
    }

    public void recordSales(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("销量不能为负数");
        }
        this.sales = (this.sales != null ? this.sales : 0) + quantity;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}