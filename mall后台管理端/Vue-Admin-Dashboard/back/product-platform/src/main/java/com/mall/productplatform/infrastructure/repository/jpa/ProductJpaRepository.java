package com.mall.productplatform.infrastructure.repository.jpa;

import com.mall.productplatform.domain.entity.Product;
import com.mall.productplatform.domain.vo.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 商品JPA仓储接口
 * 提供基础的CRUD操作和自定义查询
 */
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    /**
     * 根据状态查询商品
     */
    List<Product> findByStatus(ProductStatus status);

    /**
     * 根据状态分页查询商品
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    /**
     * 根据名称模糊查询商品
     */
    List<Product> findByNameContaining(String name);

    /**
     * 根据名称模糊分页查询商品
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据分类ID查询商品
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据分类ID分页查询商品
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * 根据商家ID查询商品
     */
    @Query("SELECT p FROM Product p WHERE p.merchant.id = :merchantId")
    List<Product> findByMerchantId(@Param("merchantId") Long merchantId);

    /**
     * 根据商家ID分页查询商品
     */
    @Query("SELECT p FROM Product p WHERE p.merchant.id = :merchantId")
    Page<Product> findByMerchantId(@Param("merchantId") Long merchantId, Pageable pageable);
} 