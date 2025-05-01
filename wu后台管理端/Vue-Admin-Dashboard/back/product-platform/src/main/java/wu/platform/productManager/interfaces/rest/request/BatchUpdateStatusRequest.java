package wu.platform.productManager.interfaces.rest.request;

import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 批量更新状态请求
 * 用于批量更新商品状态的请求数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchUpdateStatusRequest {

    /**
     * 商品ID集合
     */
    @NotEmpty(message = "商品ID列表不能为空")
    private List<Long> ids;

    /**
     * 目标状态
     */
    @NotNull(message = "状态不能为空")
    private ProductStatus status;
} 