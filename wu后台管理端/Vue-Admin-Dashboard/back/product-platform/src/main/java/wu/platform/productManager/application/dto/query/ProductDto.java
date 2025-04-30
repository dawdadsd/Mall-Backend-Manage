package wu.platform.productManager.application.dto.query;

import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品DTO
 * 用于传输商品数据到前端
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品分类
     */
    private CategoryDto category;

    /**
     * 商家
     */
    private MerchantDto merchant;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品状态
     */
    private ProductStatus status;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品库存
     */
    private Integer inventory;

    /**
     * 商品销量
     */
    private Integer sales;

    /**
     * 商品条件
     */
    private ProductCondition condition;

    /**
     * 商品图片列表
     */
    private List<String> images = new ArrayList<>();

    /**
     * 商品规格列表
     */
    private List<SpecificationViewDto> specifications = new ArrayList<>();

    /**
     * 商品SKU列表
     */
    private List<ProductSkuDto> skus = new ArrayList<>();

    /**
     * 创建时间
     */
    private Instant createdAt;

    /**
     * 最后更新时间
     */
    private Instant updatedAt;
} 