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
 * 商品详情DTO - 包含完整的商品信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO implements Serializable {
    
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
    private String conditionDescription;
    private List<ProductImageDTO> images;
    private List<ProductAttributeDTO> attributes;
    private List<ProductSpecificationDTO> specifications;
    private List<ProductSkuDTO> skus;
    private String mainImage;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;
    
    /**
     * 规格DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSpecificationDTO implements Serializable {
        private Long id;
        private String name;
        private Integer sort;
        private Boolean isRequired;
        private List<SpecificationValueDTO> values;
    }
    
    /**
     * 规格值DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecificationValueDTO implements Serializable {
        private Long id;
        private String value;
        private String displayValue;
        private Integer sort;
        private String colorCode;
        private String imageUrl;
    }
    
    /**
     * SKU DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSkuDTO implements Serializable {
        private Long id;
        private String skuCode;
        private BigDecimal price;
        private Integer inventory;
        private Integer sales;
        private String imageUrl;
        private List<SpecificationValueDTO> specificationValues;
    }
    
    /**
     * 商品属性DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttributeDTO implements Serializable {
        private Long id;
        private String name;
        private String value;
        private Integer sort;
        private Boolean isFilterable;
        private Boolean isHighlighted;
    }
} 