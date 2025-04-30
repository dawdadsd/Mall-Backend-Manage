package com.mall.productplatform.infrastructure.repository;

import com.mall.productplatform.domain.entity.Category;
import com.mall.productplatform.domain.repository.CategoryRepository;
import com.mall.productplatform.infrastructure.repository.jpa.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓储实现类
 * 负责分类实体的持久化和查询
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public List<Category> findAllRootCategories() {
        return categoryJpaRepository.findByParentIsNull();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return categoryJpaRepository.findByParentId(parentId);
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryJpaRepository.findByNameContaining(name);
    }

    @Override
    public void delete(Category category) {
        categoryJpaRepository.delete(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
} 