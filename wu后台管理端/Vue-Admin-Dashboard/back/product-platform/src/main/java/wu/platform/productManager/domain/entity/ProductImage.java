package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 商品图片实体
 * 存储产品的图片信息
 */
@Entity
@Table(name = "product_images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
public class ProductImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;
    
    @Column(name = "width")
    private Integer width;
    
    @Column(name = "height")
    private Integer height;
    
    @Column(name = "size")
    private Long size;
    
    @Column(name = "alt")
    private String alt;
    
    /**
     * 设置为主图
     */
    public void setAsPrimary() {
        this.primary = true;
    }
    
    /**
     * 取消主图状态
     */
    public void unsetPrimary() {
        this.primary = false;
    }
    
    /**
     * 获取图片尺寸文本描述
     */
    public String getDimensionText() {
        if (width != null && height != null) {
            return width + "x" + height;
        }
        return "未知尺寸";
    }
} 