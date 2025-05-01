package wu.platform.productManager.application.dto.command;

import wu.platform.productManager.application.dto.specification.SpecificationDto;
import wu.platform.productManager.domain.vo.ProductCondition;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建商品命令
 * 用于接收创建商品的请求数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand {

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;

    /**
     * 商品分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    /**
     * 商家ID
     */
    @NotNull(message = "商家ID不能为空")
    private Long merchantId;

    /**
     * 商品价格
     */
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品状态
     */
    @NotNull(message = "商品状态不能为空")
    private ProductStatus status;

    /**
     * 商品品相
     */
    @NotNull(message = "商品品相不能为空")
    private ProductCondition condition;

    /**
     * 商品标签列表
     */
    @Builder.Default
    private List<String> tags = List.of();

    /**
     * 商品属性列表
     */
    @Builder.Default
    private Map<String, String> attributes = Map.of();

    /**
     * 商品图片URL列表
     */
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    /**
     * 商品SKU列表
     */
    private List<CreateProductSkuCommand> skus = new ArrayList<>();

    /**
     * 商品规格列表
     */
    private List<CreateProductSpecificationCommand> specifications = new ArrayList<>();
} 