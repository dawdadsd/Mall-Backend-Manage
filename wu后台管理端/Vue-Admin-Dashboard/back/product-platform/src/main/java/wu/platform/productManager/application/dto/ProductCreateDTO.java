package wu.platform.productManager.application.dto;

import wu.platform.productManager.domain.vo.ProductCondition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
 * 商品创建DTO - 用于API请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO implements Serializable {
    
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotNull(message = "商家ID不能为空")
    private Long merchantId;
    
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能为负数")
    private BigDecimal price;
    
    @Size(max = 2000, message = "商品描述长度不能超过2000个字符")
    private String description;
    
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer inventory;
    
    @NotNull(message = "品相评级不能为空")
    private ProductCondition condition;
    
    private List<@Valid ProductAttributeCreateDTO> attributes;
    
    private List<@Valid ProductSpecificationCreateDTO> specifications;
    
    private List<@Valid ProductSkuCreateDTO> skus;
    
    /**
     * 商品属性创建DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttributeCreateDTO implements Serializable {
        
        @NotBlank(message = "属性名称不能为空")
        @Size(max = 50, message = "属性名称长度不能超过50个字符")
        private String name;
        
        @NotBlank(message = "属性值不能为空")
        @Size(max = 100, message = "属性值长度不能超过100个字符")
        private String value;
        
        private Integer sort;
        
        private Boolean isFilterable;
        
        private Boolean isHighlighted;
    }
    
    /**
     * 商品规格创建DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSpecificationCreateDTO implements Serializable {
        
        @NotBlank(message = "规格名称不能为空")
        @Size(max = 50, message = "规格名称长度不能超过50个字符")
        private String name;
        
        private Integer sort;
        
        private Boolean isRequired;
        
        @NotNull(message = "规格值列表不能为空")
        @Size(min = 1, message = "规格至少需要一个规格值")
        private List<@Valid SpecificationValueCreateDTO> values;
    }
    
    /**
     * 规格值创建DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecificationValueCreateDTO implements Serializable {
        
        @NotBlank(message = "规格值不能为空")
        @Size(max = 50, message = "规格值长度不能超过50个字符")
        private String value;
        
        private String displayValue;
        
        private Integer sort;
        
        private String colorCode;
        
        private String imageUrl;
    }
    
    /**
     * SKU创建DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSkuCreateDTO implements Serializable {
        
        @NotBlank(message = "SKU编码不能为空")
        @Size(max = 50, message = "SKU编码长度不能超过50个字符")
        private String skuCode;
        
        @NotNull(message = "价格不能为空")
        @Min(value = 0, message = "价格不能为负数")
        private BigDecimal price;
        
        @NotNull(message = "库存不能为空")
        @Min(value = 0, message = "库存不能为负数")
        private Integer inventory;
        
        private String imageUrl;
        
        @NotNull(message = "规格值ID列表不能为空")
        @Size(min = 1, message = "SKU至少需要关联一个规格值")
        private List<Long> specificationValueIds;
    }
} 