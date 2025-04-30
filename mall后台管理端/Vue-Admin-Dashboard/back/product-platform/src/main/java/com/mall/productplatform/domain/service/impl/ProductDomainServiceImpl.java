package com.mall.productplatform.domain.service.impl;

import com.mall.productplatform.domain.entity.Product;
import com.mall.productplatform.domain.entity.ProductSku;
import com.mall.productplatform.domain.entity.ProductSpecification;
import com.mall.productplatform.domain.repository.ProductRepository;
import com.mall.productplatform.domain.service.ProductDomainService;
import com.mall.productplatform.domain.vo.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品领域服务实现类
 * 处理跨实体和聚合根的复杂业务逻辑
 */
@Service
@RequiredArgsConstructor
public class ProductDomainServiceImpl implements ProductDomainService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProductWithSpecsAndSkus(Product product, Set<ProductSpecification> specifications, Set<ProductSku> skus) {
        // 设置关联关系
        specifications.forEach(product::addSpecification);
        skus.forEach(product::addSku);
        // 计算总库存
        int totalInventory = skus.stream()
                .mapToInt(ProductSku::getInventory)
                .sum();
        product.updateInventory(totalInventory);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProductInventory(Product product, Set<ProductSku> skus) {
        // 更新SKU关联
        Set<ProductSku> existingSkus = product.getSkus();
        
        // 移除不再存在的SKU
        Set<ProductSku> toRemove = new HashSet<>(existingSkus);
        toRemove.removeAll(skus);
        
        // 添加新的SKU
        Set<ProductSku> toAdd = new HashSet<>(skus);
        toAdd.removeAll(existingSkus);
        
        // 更新现有SKU
        Set<ProductSku> toUpdate = skus.stream()
                .filter(existingSkus::contains)
                .collect(Collectors.toSet());
                
        // 对SKU进行处理（此处简化处理，实际应考虑更复杂的业务逻辑）
        toRemove.forEach(sku -> sku.setProduct(null));
        toAdd.forEach(product::addSku);
        
        // 重新计算总库存
        int totalInventory = skus.stream()
                .mapToInt(ProductSku::getInventory)
                .sum();
        product.updateInventory(totalInventory);
        
        return productRepository.save(product);
    }

    @Override
    public boolean canPutOnSale(Product product) {
        // 检查商品是否可以上架
        // 1. 库存必须大于0
        if (product.getInventory() <= 0) {
            return false;
        }
        
        // 2. 商品必须有效
        if (product.getStatus() == ProductStatus.DELETED) {
            return false;
        }
        
        // 3. 商品必须有基本信息
        if (product.getName() == null || product.getPrice() == null) {
            return false;
        }
        
        // 4. 商品必须有至少一个SKU
        return !product.getSkus().isEmpty();
    }

    @Override
    public boolean canTakeOffSale(Product product) {
        // 检查商品是否可以下架
        // 基本上任何状态的商品都可以下架，除非已经是下架状态
        return product.getStatus() != ProductStatus.OFF_SHELF && 
               product.getStatus() != ProductStatus.DELETED;
    }

    @Override
    @Transactional
    public int batchUpdateStatus(Set<Long> productIds, ProductStatus targetStatus) {
        int successCount = 0;
        
        for (Long productId : productIds) {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                
                boolean canUpdate = false;
                switch (targetStatus) {
                    case ON_SALE:
                        canUpdate = canPutOnSale(product);
                        if (canUpdate) {
                            product.putOnSale();
                        }
                        break;
                    case OFF_SHELF:
                        canUpdate = canTakeOffSale(product);
                        if (canUpdate) {
                            product.takeOffSale();
                        }
                        break;
                    // 可以添加其他状态的处理
                    default:
                        // 默认不处理
                        break;
                }
                
                if (canUpdate) {
                    productRepository.save(product);
                    successCount++;
                }
            }
        }
        
        return successCount;
    }
} 