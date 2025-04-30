package wu.platform.productManager.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类DTO
 * 用于传输分类数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 分类排序值
     */
    private Integer sortOrder;

    /**
     * 分类图标URL
     */
    private String icon;

    /**
     * 是否为叶子节点
     */
    private Boolean isLeaf;
} 