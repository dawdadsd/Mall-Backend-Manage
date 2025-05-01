package wu.platform.productManager.application.service.impl;

import wu.platform.productManager.application.dto.command.CreateCategoryCommand;
import wu.platform.productManager.application.dto.query.CategoryDto;
import wu.platform.productManager.application.service.CategoryApplicationService;
import wu.platform.productManager.common.exception.ResourceNotFoundException;
import wu.platform.productManager.domain.entity.Category;
import wu.platform.productManager.domain.repository.CategoryRepository;
import wu.platform.productManager.application.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * 商品分类应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryApplicationServiceImpl implements CategoryApplicationService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * 创建商品分类
     *
     * @param command 创建商品分类命令
     * @return 创建后的商品分类DTO
     */
    @Override
    @Transactional
    public CategoryDto createCategory(CreateCategoryCommand command) {
        log.info("创建商品分类: {}", command.getName());
        
        Category category = new Category();
        category.setName(command.getName());
        category.setCode(command.getCode());
        category.setDescription(command.getDescription());
        category.setLevel(command.getLevel());
        category.setSortOrder(command.getSortOrder());
        category.setIconUrl(command.getIconUrl());
        category.setEnabled(command.getIsEnabled() != null ? command.getIsEnabled() : true);
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        
        Category savedCategory = categoryRepository.save(category);
        
        return mapToCategoryDto(savedCategory);
    }

    /**
     * 获取商品分类详情
     *
     * @param id 商品分类ID
     * @return 商品分类DTO
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        log.info("获取商品分类: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品分类不存在: " + id));
        
        return mapToCategoryDto(category);
    }

    /**
     * 获取所有商品分类
     *
     * @param pageable 分页参数
     * @return 商品分类DTO分页结果
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        log.info("获取所有商品分类");
        
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        
        return categoryPage.map(this::mapToCategoryDto);
    }

    /**
     * 更新商品分类
     *
     * @param id      商品分类ID
     * @param command 更新商品分类命令
     * @return 更新后的商品分类DTO
     */
    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CreateCategoryCommand command) {
        log.info("更新商品分类: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品分类不存在: " + id));
        
        category.setName(command.getName());
        category.setCode(command.getCode());
        category.setDescription(command.getDescription());
        category.setLevel(command.getLevel());
        category.setSortOrder(command.getSortOrder());
        category.setIconUrl(command.getIconUrl());
        category.setEnabled(command.getIsEnabled() != null ? command.getIsEnabled() : category.isEnabled());
        category.setUpdatedAt(Instant.now());
        
        Category updatedCategory = categoryRepository.save(category);
        
        return mapToCategoryDto(updatedCategory);
    }

    /**
     * 删除商品分类
     *
     * @param id 商品分类ID
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("删除商品分类: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品分类不存在: " + id));
        
        categoryRepository.delete(category);
    }

    /**
     * 将实体映射为DTO
     *
     * @param category 商品分类实体
     * @return 商品分类DTO
     */
    private CategoryDto mapToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setLevel(category.getLevel());
        dto.setSortOrder(category.getSortOrder());
        dto.setIconUrl(category.getIconUrl());
        dto.setIsEnabled(category.isEnabled());
        dto.setIsLeaf(category.isLeaf());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
} 