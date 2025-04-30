package com.mall.productplatform.domain.repository;

import com.mall.productplatform.domain.entity.Product;
import com.mall.productplatform.domain.vo.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品仓储接口
 * 定义领域模型 Product 的持久化和检索能力
 * 遵循 DDD 设计原则，接口在领域层定义，实现在基础设施层
 */
public interface ProductRepository {

    /**
     * 保存商品
     * @param product 商品实体
     * @return 持久化后的商品实体
     */
    Product save(Product product);

    /**
     * 根据ID查找商品
     * @param id 商品ID
     * @return 可能包含商品的 Optional
     */
    Optional<Product> findById(Long id);

    /**
     * 根据商品名称查找商品
     * @param name 商品名称
     * @return 商品列表
     */
    List<Product> findByName(String name);

    /**
     * 根据商品状态查找商品
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> findByStatus(ProductStatus status);

    /**
     * 根据分类ID查找商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * 根据商家ID查找商品
     * @param merchantId 商家ID
     * @return 商品列表
     */
    List<Product> findByMerchantId(Long merchantId);

    /**
     * 删除商品
     * @param product 商品实体
     */
    void delete(Product product);

    /**
     * 根据ID删除商品
     * @param id 商品ID
     */
    void deleteById(Long id);
    
    /**
     * 分页查询商品
     * 
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * 根据状态分页查询商品
     * 
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    /**
     * 根据分类ID分页查询商品
     * 
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 根据商家ID分页查询商品
     * 
     * @param merchantId 商家ID
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    Page<Product> findByMerchantId(Long merchantId, Pageable pageable);

    /**
     * 根据名称分页查询商品
     * 
     * @param name 商品名称
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);
} 