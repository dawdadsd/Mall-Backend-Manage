package wu.platform.productManager.application.dto;

import wu.platform.productManager.domain.vo.ProductCondition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品品相更新DTO
 * 用于更新商品的品相评级
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConditionUpdateDTO implements Serializable {
    
    @NotNull(message = "品相不能为空")
    private ProductCondition condition;
    
    private String assessmentNote;
    
    @NotNull(message = "版本号不能为空")
    private Integer version;
} 