package wu.platform.productManager.application.dto.query;

import lombok.AllArgsConstructor;
// import lombok.Builder; // Temporarily remove builder
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU DTO
 * 用于展示SKU信息
 */
@Data
// @Builder // Temporarily remove builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuDto {
    
    private Long id;
    
    /**
     * SKU编码
     */
    private String skuCode;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 库存数量
     */
    private Integer inventory;
    
    /**
     * 销量
     */
    private Integer sales;
    
    /**
     * SKU图片URL
     */
    private String imageUrl;
    
    /**
     * 规格组合
     * (例如: {"颜色": "红色", "尺寸": "L"})
     */
    private Map<String, String> specifications;
    
    /**
     * 是否启用
     */
    private Boolean isEnabled; // Note: No @Builder.Default needed if @Builder is removed
    
    /**
     * 是否已验证 (假设这个字段是需要的，即使ProductSku没有)
     */
    private Boolean isVerified; 
} 