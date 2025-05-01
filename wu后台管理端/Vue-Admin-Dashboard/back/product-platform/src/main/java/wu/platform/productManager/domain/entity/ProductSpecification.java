package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品规格实体
 * 代表商品的某个规格，例如"颜色"、"尺寸"
 */
@Entity
@Table(name = "product_specifications")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product", "values"})
public class ProductSpecification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    /**
     * 规格名称 (例如: 颜色)
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    /**
     * 规格值列表 (例如: 红色, 蓝色)
     * 使用 OneToMany 关联 SpecificationValue 实体
     */
    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Builder.Default
    private List<SpecificationValue> values = new ArrayList<>();
    
    /**
     * 添加规格值
     */
    public void addValue(String value) {
        SpecificationValue specValue = new SpecificationValue();
        specValue.setValue(value);
        specValue.setSpecification(this);
        this.values.add(specValue);
    }
    
    /**
     * 移除规格值
     */
    public void removeValue(String value) {
        this.values.removeIf(v -> v.getValue().equals(value));
    }
}