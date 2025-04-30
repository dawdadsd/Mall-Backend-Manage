package wu.platform.productManager.application.mapper;

import wu.platform.productManager.application.dto.query.CategoryDto;
import wu.platform.productManager.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 分类对象映射器
 * 使用MapStruct进行对象映射
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * 将分类实体转换为DTO
     * 
     * @param category 分类实体
     * @return 分类DTO
     */
    @Mapping(target = "isLeaf", expression = "java(category.getChildren() == null || category.getChildren().isEmpty())")
    CategoryDto toDto(Category category);

    /**
     * 将分类实体列表转换为DTO列表
     * 
     * @param categories 分类实体列表
     * @return 分类DTO列表
     */
    List<CategoryDto> toDtoList(List<Category> categories);
} 