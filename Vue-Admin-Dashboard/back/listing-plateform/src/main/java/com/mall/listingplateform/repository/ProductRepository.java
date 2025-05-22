package com.mall.listingplateform.repository;

import com.mall.listingplateform.model.Product;
import com.mall.listingplateform.model.Product.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 商品资源库
 * 
 * 包含针对商品实体的基本CRUD操作和自定义查询方法
 * 采用JPA Specification支持复杂动态查询
 * 
 * @since 2025
 * @version 2.0
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    /**
     * 根据商品编码查找商品
     * 
     * @param code 商品唯一编码
     * @return 商品Optional包装对象
     */
    Optional<Product> findByCode(String code);
    
    /**
     * 根据商品名称模糊查询
     * 
     * @param name 商品名称关键词
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    /**
     * 查询指定分类下的商品
     * 
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * 查询指定状态的商品
     * 
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Product> findByStatus(Status status, Pageable pageable);
    
    /**
     * 查询价格区间内的商品
     * 
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    @Query("SELECT p FROM Product p WHERE p.sellingPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
    
    /**
     * 查询指定时间段内创建的商品
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Product> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    /**
     * 查询库存低于警戒值的商品
     * 
     * @return 低库存商品列表
     */
    @Query("SELECT p FROM Product p WHERE p.stock <= p.warningStock AND p.status = 'PUBLISHED'")
    List<Product> findLowStockProducts();
    
    /**
     * 统计各状态商品数量
     * 
     * @return 状态及对应商品数量
     */
    @Query("SELECT p.status, COUNT(p) FROM Product p GROUP BY p.status")
    List<Object[]> countByStatus();
    
    /**
     * 查询最近更新的商品
     * 
     * @param limit 限制数量
     * @return 最近更新商品列表
     */
    @Query(value = "SELECT * FROM plateform-product ORDER BY update_time DESC LIMIT :limit", nativeQuery = true)
    List<Product> findRecentlyUpdated(@Param("limit") int limit);
    
    /**
     * 检查商品编码是否已存在
     * 
     * @param code 商品编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据多个ID批量查询商品
     * 
     * @param ids ID集合
     * @return 商品列表
     */
    List<Product> findByIdIn(List<Long> ids);
}
