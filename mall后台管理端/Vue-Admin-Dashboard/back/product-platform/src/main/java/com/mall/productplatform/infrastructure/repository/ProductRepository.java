package com.mall.productplatform.infrastructure.repository;

import com.mall.productplatform.domain.entity.Product;
import com.mall.productplatform.domain.vo.ProductCondition;
import com.mall.productplatform.domain.vo.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 商品仓库接口
 * 提供丰富的商品查询和管理功能
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * 使用EntityGraph查询商品详情（包含关联实体）
     */
    @EntityGraph(attributePaths = {"images", "category", "merchant"})
    Optional<Product> findWithDetailById(Long id);
    
    /**
     * 按类别查询商品，分页
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * 按状态查询商品，分页
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    /**
     * 按商家查询商品，分页
     */
    Page<Product> findByMerchantId(Long merchantId, Pageable pageable);
    
    /**
     * 按品相查询商品，分页
     */
    Page<Product> findByCondition(ProductCondition condition, Pageable pageable);
    
    /**
     * 按价格区间查询商品
     */
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    /**
     * 查询低库存商品
     */
    @Query("SELECT p FROM Product p WHERE p.inventory <= :threshold AND p.status = 'ON_SALE'")
    List<Product> findLowInventoryProducts(@Param("threshold") int threshold);
    
    /**
     * 查询热销商品
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ON_SALE' ORDER BY p.sales DESC")
    Page<Product> findHotSellingProducts(Pageable pageable);
    
    /**
     * 按名称和描述关键词搜索商品
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ON_SALE' AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查询指定商品ID列表的商品
     */
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findByIdIn(@Param("ids") List<Long> ids);
    
    /**
     * 按分类代码查询商品
     */
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.code = :categoryCode")
    Page<Product> findByCategoryCode(@Param("categoryCode") String categoryCode, Pageable pageable);
    
    /**
     * 查询新增商品（最近上传）
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ON_SALE' ORDER BY p.createdAt DESC")
    Page<Product> findNewlyAddedProducts(Pageable pageable);
} 