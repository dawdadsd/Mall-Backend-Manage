package wu.platform.productManager.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建商品规格命令
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductSpecificationCommand {

    @NotBlank(message = "规格名称不能为空")
    @Size(max = 50, message = "规格名称长度不能超过50个字符")
    private String name;

    @NotEmpty(message = "规格值列表不能为空")
    private List<@NotBlank @Size(max = 50) String> values;
} 