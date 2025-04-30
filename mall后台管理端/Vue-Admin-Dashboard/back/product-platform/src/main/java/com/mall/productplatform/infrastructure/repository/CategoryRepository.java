package com.mall.productplatform.infrastructure.repository;

import com.mall.productplatform.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品分类仓库接口
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据代码查找分类
     */
    Optional<Category> findByCode(String code);
    
    /**
     * 查找根分类
     */
    List<Category> findByParentIsNull();
    
    /**
     * 查找子分类
     */
    List<Category> findByParentId(Long parentId);
    
    /**
     * 查找指定层级的分类
     */
    List<Category> findByLevel(Integer level);
    
    /**
     * 根据名称关键词搜索分类
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 查找启用的分类
     */
    List<Category> findByIsEnabledTrue();
    
    /**
     * 检查代码是否唯一
     */
    boolean existsByCode(String code);
    
    /**
     * 根据代码列表查找分类列表
     */
    List<Category> findByCodeIn(List<String> codes);
    
    /**
     * 查询分类树
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL ORDER BY c.sortOrder ASC")
    List<Category> findCategoryTree();
} 