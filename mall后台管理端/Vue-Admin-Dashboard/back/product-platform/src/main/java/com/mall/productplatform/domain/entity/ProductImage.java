package com.mall.productplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 商品图片实体类
 */
@Entity
@Table(name = "product_images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "url", nullable = false)
    private String url;
    
    @Column(name = "sort", nullable = false)
    private Integer sort;
    
    @Column(name = "is_main", nullable = false)
    private Boolean isMain;
    
    @Column(name = "alt_text")
    private String altText;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @Column(name = "width")
    private Integer width;
    
    @Column(name = "height")
    private Integer height;
    
    /**
     * 设置为主图
     */
    public void setAsMain() {
        this.isMain = true;
    }
    
    /**
     * 取消主图设置
     */
    public void unsetAsMain() {
        this.isMain = false;
    }
} 