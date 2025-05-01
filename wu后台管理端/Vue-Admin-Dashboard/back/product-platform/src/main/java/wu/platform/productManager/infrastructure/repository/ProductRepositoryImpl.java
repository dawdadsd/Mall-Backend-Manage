package wu.platform.productManager.infrastructure.repository;

import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.repository.ProductRepository;
import wu.platform.productManager.domain.vo.ProductStatus;
import wu.platform.productManager.infrastructure.repository.jpa.ProductJpaRepository;
import wu.platform.productManager.infrastructure.repository.jpa.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 商品仓储实现类
 * 负责商品实体的持久化和查询
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<Product> findByName(String name) {
        return productJpaRepository.findByNameContaining(name);
    }

    @Override
    public List<Product> findByStatus(ProductStatus status) {
        return productJpaRepository.findByStatus(status);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productJpaRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> findByMerchantId(Long merchantId) {
        return productJpaRepository.findByMerchantId(merchantId);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public List<Product> findAllById(Iterable<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }

    /**
     * 分页查询商品
     * 
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    /**
     * 根据状态分页查询商品
     * 
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    @Override
    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return productJpaRepository.findByStatus(status, pageable);
    }

    /**
     * 根据分类ID分页查询商品
     * 
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productJpaRepository.findByCategoryId(categoryId, pageable);
    }

    /**
     * 根据商家ID分页查询商品
     * 
     * @param merchantId 商家ID
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    @Override
    public Page<Product> findByMerchantId(Long merchantId, Pageable pageable) {
        return productJpaRepository.findByMerchantId(merchantId, pageable);
    }

    /**
     * 根据名称分页查询商品
     * 
     * @param name 商品名称
     * @param pageable 分页参数
     * @return 商品分页结果
     */
    @Override
    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        return productJpaRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Product> findByCriteria(Long categoryId, Long merchantId, ProductStatus status, String keyword, Pageable pageable) {
        // Build JPA Specification based on criteria
        Specification<Product> spec = ProductSpecification.byCriteria(categoryId, merchantId, status, keyword);
        return productJpaRepository.findAll(spec, pageable);
        
        // // Placeholder implementation - replace with actual Specification logic
        // System.err.println("Warning: ProductRepositoryImpl.findByCriteria is not fully implemented.");
        // if (keyword != null && !keyword.isEmpty()) {
        //     return findByNameContaining(keyword, pageable);
        // } else {
        //     return findAll(pageable);
        // }
    }
} 