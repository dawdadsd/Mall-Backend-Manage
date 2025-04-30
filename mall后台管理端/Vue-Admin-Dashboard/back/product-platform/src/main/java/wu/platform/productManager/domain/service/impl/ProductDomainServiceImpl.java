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

    /**
     * 创建商品关联
     * @param product 商品实体
     * @param specifications 规格列表
     * @param skus SKU列表
     * @return 保存的商品实体类
     */
    @Override
    @Transactional
    public Product createProductWithSpecsAndSkus(Product product, Set<ProductSpecification> specifications, Set<ProductSku> skus) {
        specifications.forEach(product::addSpecification);
        skus.forEach(product::addSku);
        int totalInventory = skus.stream()
                .mapToInt(ProductSku :: getInventory )
                .sum();
        product.updateInventory(totalInventory);
        return productRepository.save(product);
    }

    /**
     *
     * @param product 商品实体
     * @param skus 更新后的SKU列表
     * @return 更新后的商品实体类
     */
    @Override
    @Transactional
    public Product updateProductInventory(Product product, Set<ProductSku> skus) {
        Set<ProductSku> existingSkus = product.getSkus();
        Set<ProductSku> toRemove = new HashSet<>(existingSkus);
        toRemove.removeAll(skus);
        Set<ProductSku> toAdd = new HashSet<>(skus);
        toAdd.removeAll(existingSkus);
        Set<ProductSku> toUpdate = skus.stream()
                .filter(existingSkus::contains)
                .collect(Collectors.toSet());
        toRemove.forEach(sku -> sku.setProduct(null));
        toAdd.forEach(product::addSku);
        int totalInventory = skus.stream()
                .mapToInt(ProductSku::getInventory)
                .sum();
        product.updateInventory(totalInventory);
        return productRepository.save(product);
    }

    /**
     * 判断是否可以上架
     * @param product 商品实体
     * @return 是否可以上架
     */
    @Override
    public boolean canPutOnSale(Product product) {
        if(product.getInventory() <= 0)
        {
            return false;
        }
        if(product.getName() == null || product.getPrice() == null)
        {
            return false;
        }
        if(product.getStatus() == ProductStatus.DELETED)
        {
            return false;
        }
        return !product.getSkus().isEmpty();
    }

    /**
     * 检查商品是否可以下架
     * @param product 商品实体
     * @return true:可以 false:不可以
     */
    @Override
    public boolean canTakeOffSale(Product product) {
        return product.getStatus() != ProductStatus.DELETED &&
               product.getStatus() != ProductStatus.OFF_SHELF;
    }

    /**
     * 批处理商品状态
     * @param productIds 商品ID列表
     * @param targetStatus 目标状态
     * @return 成功处理的数量
     */
    @Override
    @Transactional
    public int batchUpdateStatus(Set<Long> productIds, ProductStatus targetStatus) {
        int successCount = 0;
        for(Long productId : productIds){
            Optional<Product> productOpt = productRepository.findById(productId);
            if(productOpt.isPresent()){
                Product product = productOpt.get();
                boolean canUpdate = false;
                switch(targetStatus){
                    case ON_SALE:
                        canUpdate = canPutOnSale(product);
                        if(canUpdate){
                            product.putOnSale();
                        }
                        break;
                    case OFF_SHELF:
                        canUpdate = canTakeOffSale(product);
                        if(canUpdate){
                            product.takeOffSale();
                        }
                        break;
                    default:
                        break;
                }
                if(canUpdate)
                {
                    productRepository.save(product);
                    successCount++;
                }
            }
        }
        return successCount;
    }
} 