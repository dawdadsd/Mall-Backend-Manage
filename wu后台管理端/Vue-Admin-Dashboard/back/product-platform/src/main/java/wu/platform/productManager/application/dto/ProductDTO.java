package wu.platform.productManager.application.dto;

import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 商品DTO - 用于API响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {
    
    private Long id;
    private String name;
    private CategoryDTO category;
    private MerchantDTO merchant;
    private BigDecimal price;
    private ProductStatus status;
    private String statusDisplayName;
    private String description;
    private Integer inventory;
    private Integer sales;
    private ProductCondition condition;
    private String conditionLabel;
    private List<ProductImageDTO> images;
    private String mainImage;
    private Instant createdAt;
    private Instant updatedAt;
    
    /**
     * 简化版DTO - 用于列表显示
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummaryDTO implements Serializable {
        private Long id;
        private String name;
        private String categoryName;
        private String merchantName;
        private BigDecimal price;
        private String status;
        private Integer inventory;
        private Integer sales;
        private String condition;
        private String mainImage;
    }
} 