package wu.platform.productManager.application.service;

import wu.platform.productManager.application.dto.command.CreateCategoryCommand;
import wu.platform.productManager.application.dto.query.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商品分类应用服务接口
 */
public interface CategoryApplicationService {
    
    /**
     * 创建商品分类
     * 
     * @param command 创建商品分类命令
     * @return 创建后的商品分类DTO
     */
    CategoryDto createCategory(CreateCategoryCommand command);
    
    /**
     * 获取商品分类详情
     * 
     * @param id 商品分类ID
     * @return 商品分类DTO
     */
    CategoryDto getCategory(Long id);
    
    /**
     * 获取所有商品分类
     * 
     * @param pageable 分页参数
     * @return 商品分类DTO分页结果
     */
    Page<CategoryDto> getAllCategories(Pageable pageable);
    
    /**
     * 更新商品分类
     * 
     * @param id 商品分类ID
     * @param command 更新商品分类命令
     * @return 更新后的商品分类DTO
     */
    CategoryDto updateCategory(Long id, CreateCategoryCommand command);
    
    /**
     * 删除商品分类
     * 
     * @param id 商品分类ID
     */
    void deleteCategory(Long id);
} 