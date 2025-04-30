package wu.platform.productManager.application.dto.command;

import wu.platform.productManager.application.dto.specification.SpecificationDto;
import wu.platform.productManager.domain.vo.ProductCondition;
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
    @Size(max = 100, message = "商品名称不能超过100个字符")
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
    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    private BigDecimal price;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品状态
     */
    private String status;

    /**
     * 商品条件（新品、二手等）
     */
    @NotNull(message = "商品条件不能为空")
    private ProductCondition condition;

    /**
     * 商品规格列表
     */
    private List<SpecificationDto> specifications = new ArrayList<>();

    /**
     * 商品SKU列表
     */
    private List<CreateProductSkuCommand> skus = new ArrayList<>();

    /**
     * 商品图片URL列表
     */
    private List<String> imageUrls = new ArrayList<>();
} 