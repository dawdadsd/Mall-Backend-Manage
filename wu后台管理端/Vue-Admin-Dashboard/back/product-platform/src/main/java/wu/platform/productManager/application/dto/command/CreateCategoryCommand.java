package wu.platform.productManager.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 创建商品分类命令
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryCommand {
    
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String name;
    
    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 20, message = "分类编码长度不能超过20个字符")
    private String code;
    
    /**
     * 分类描述
     */
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
    
    /**
     * 分类级别
     */
    private Integer level;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    
    /**
     * 图标URL
     */
    private String iconUrl;
    
    /**
     * 是否启用
     */
    private Boolean isEnabled;
} 