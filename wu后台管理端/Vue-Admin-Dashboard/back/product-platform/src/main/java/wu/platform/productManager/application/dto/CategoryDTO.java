package wu.platform.productManager.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分类DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer level;
    private Integer sortOrder;
    private String iconUrl;
    private Boolean isEnabled;
    private Long parentId;
    private String parentName;
    private List<CategoryDTO> children;
    
    /**
     * 简化版DTO - 只包含基本信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBasicDTO implements Serializable {
        private Long id;
        private String name;
        private String code;
        private Integer level;
        private Boolean hasChildren;
    }
}