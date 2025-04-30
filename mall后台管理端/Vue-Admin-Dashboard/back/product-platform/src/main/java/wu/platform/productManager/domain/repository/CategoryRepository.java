package com.mall.productplatform.domain.repository;

import com.mall.productplatform.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * 商品分类仓储接口
 * 定义领域模型 Category 的持久化和检索能力
 */
public interface CategoryRepository {

    /**
     * 保存分类
     * @param category 分类实体
     * @return 持久化后的分类实体
     */
    Category save(Category category);

    /**
     * 根据ID查找分类
     * @param id 分类ID
     * @return 可能包含分类的 Optional
     */
    Optional<Category> findById(Long id);

    /**
     * 查找所有分类
     * @return 分类列表
     */
    List<Category> findAll();

    /**
     * 查找所有顶级分类（没有父分类的分类）
     * @return 顶级分类列表
     */
    List<Category> findAllRootCategories();

    /**
     * 根据父分类ID查找子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> findByParentId(Long parentId);

    /**
     * 根据分类名称查找分类
     * @param name 分类名称
     * @return 分类列表
     */
    List<Category> findByName(String name);

    /**
     * 删除分类
     * @param category 分类实体
     */
    void delete(Category category);

    /**
     * 根据ID删除分类
     * @param id 分类ID
     */
    void deleteById(Long id);
} 