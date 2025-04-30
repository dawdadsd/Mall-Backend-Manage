package wu.platform.productManager.application.dto;

import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品更新DTO - 用于API请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO implements Serializable {
    
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;
    
    private Long categoryId;
    
    private Long merchantId;
    
    @Min(value = 0, message = "价格不能为负数")
    private BigDecimal price;
    
    private ProductStatus status;
    
    @Size(max = 2000, message = "商品描述长度不能超过2000个字符")
    private String description;
    
    @Min(value = 0, message = "库存不能为负数")
    private Integer inventory;
    
    private ProductCondition condition;
    
    private List<@Valid ProductAttributeUpdateDTO> attributes;
    
    private List<@Valid ProductSpecificationUpdateDTO> specifications;
    
    private List<@Valid ProductSkuUpdateDTO> skus;
    
    private List<Long> imagesToRemove;
    
    /**
     * 版本号（用于乐观锁）
     */
    @NotNull(message = "版本号不能为空")
    private Integer version;
    
    /**
     * 商品属性更新DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttributeUpdateDTO implements Serializable {
        
        private Long id;
        
        @Size(max = 50, message = "属性名称长度不能超过50个字符")
        private String name;
        
        @Size(max = 100, message = "属性值长度不能超过100个字符")
        private String value;
        
        private Integer sort;
        
        private Boolean isFilterable;
        
        private Boolean isHighlighted;
    }
    
    /**
     * 商品规格更新DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSpecificationUpdateDTO implements Serializable {
        
        private Long id;
        
        @Size(max = 50, message = "规格名称长度不能超过50个字符")
        private String name;
        
        private Integer sort;
        
        private Boolean isRequired;
        
        private List<@Valid SpecificationValueUpdateDTO> values;
        
        private List<Long> valuesToRemove;
    }
    
    /**
     * 规格值更新DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecificationValueUpdateDTO implements Serializable {
        
        private Long id;
        
        @Size(max = 50, message = "规格值长度不能超过50个字符")
        private String value;
        
        private String displayValue;
        
        private Integer sort;
        
        private String colorCode;
        
        private String imageUrl;
    }
    
    /**
     * SKU更新DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSkuUpdateDTO implements Serializable {
        
        private Long id;
        
        @Size(max = 50, message = "SKU编码长度不能超过50个字符")
        private String skuCode;
        
        @Min(value = 0, message = "价格不能为负数")
        private BigDecimal price;
        
        @Min(value = 0, message = "库存不能为负数")
        private Integer inventory;
        
        private String imageUrl;
        
        private List<Long> specificationValueIds;
    }
} 