package wu.platform.productManager.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 创建商品SKU命令
 * 用于接收创建商品SKU的请求数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductSkuCommand {

    /**
     * SKU编码
     */
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;

    /**
     * SKU价格
     */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    /**
     * SKU库存
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer inventory;

    /**
     * SKU图片URL
     */
    private String imageUrl;

    /**
     * SKU规格组合
     * 例如: {"颜色": "红色", "尺寸": "L"}
     */
    @NotNull(message = "规格组合不能为空")
    private Map<String, String> specifications;

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean enabled = true;
} 