package wu.platform.productManager.domain.service.impl;

import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.entity.ProductSku;
import wu.platform.productManager.domain.entity.ProductSpecification;
import wu.platform.productManager.domain.repository.ProductRepository;
import wu.platform.productManager.domain.service.ProductDomainService;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品领域服务实现类
 * 处理跨实体和聚合根的复杂业务逻辑
 */
@Slf4j
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
    public Product createProductWithSpecsAndSkus(Product product, List<ProductSpecification> specifications, List<ProductSku> skus) {
        product.getSpecifications().clear();
        product.getSkus().clear();
        
        if (specifications != null) {
            specifications.forEach(product::addSpecification);
        }
        if (skus != null) {
            skus.forEach(product::addSku);
        }
        
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
    public Product updateProductInventory(Product product, List<ProductSku> updatedSkus) {
        log.warn("updateProductInventory is complex and might need refinement based on exact requirements.");
        Set<ProductSku> existingSkus = new HashSet<>(product.getSkus());
        Set<ProductSku> newSkus = new HashSet<>(updatedSkus);

        Set<ProductSku> toRemove = new HashSet<>(existingSkus);
        toRemove.removeAll(newSkus);

        Set<ProductSku> toAdd = new HashSet<>(newSkus);
        toAdd.removeAll(existingSkus);
        
        log.info("Updating inventory for product ID {}. Adding: {}, Removing: {}. Current existing: {}.", 
                 product.getId(), toAdd.size(), toRemove.size(), existingSkus.size());

        toRemove.forEach(sku -> product.removeSku(sku.getSkuCode()));
        
        toAdd.forEach(product::addSku);
        
        for (ProductSku updatedSku : updatedSkus) {
            Optional<ProductSku> existingOpt = existingSkus.stream()
                .filter(e -> e.getSkuCode().equals(updatedSku.getSkuCode()))
                .findFirst();
            
            if (existingOpt.isPresent()) {
                ProductSku existing = existingOpt.get();
                existing.setPrice(updatedSku.getPrice());
                existing.setInventory(updatedSku.getInventory());
                existing.setEnabled(updatedSku.isEnabled());
                log.debug("Updating existing SKU: {}", existing.getSkuCode());
            } else {
                 log.warn("Updated SKU {} was not found in existing set, should have been added.", updatedSku.getSkuCode());
            }
        }

        return productRepository.save(product);
    }

    /**
     * 判断是否可以上架
     * @param product 商品实体
     * @return 是否可以上架
     */
    @Override
    public boolean canPutOnSale(Product product) {
        boolean hasStock = product.getSkus().stream()
                              .anyMatch(sku -> sku.isEnabled() && sku.getInventory() > 0);
                              
        return hasStock && 
               product.getStatus() != ProductStatus.DELETED && 
               product.getStatus() != ProductStatus.ON_SALE;
    }

    /**
     * 检查商品是否可以下架
     * @param product 商品实体
     * @return true:可以 false:不可以
     */
    @Override
    public boolean canTakeOffSale(Product product) {
        return product.getStatus() == ProductStatus.ON_SALE || product.getStatus() == ProductStatus.SOLD_OUT;
    }

    /**
     * 批处理商品状态
     * @param productIds 商品ID列表
     * @param targetStatus 目标状态
     * @return 成功处理的数量
     */
    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> productIds, ProductStatus targetStatus) {
        int successCount = 0;
        List<Product> products = productRepository.findAllById(productIds);
        
        log.info("Attempting to batch update status for {} products to {}", products.size(), targetStatus);

        for(Product product : products){
            try {
                boolean canUpdate = false;
                ProductStatus previousStatus = product.getStatus();

                switch(targetStatus){
                    case ON_SALE:
                        if (canPutOnSale(product)) {
                            product.updateStatus(ProductStatus.ON_SALE);
                            canUpdate = true;
                        } else {
                            log.warn("Product ID {} cannot be put ON_SALE (status: {}, stock check failed?)", product.getId(), previousStatus);
                        }
                        break;
                    case OFF_SHELF:
                         if (canTakeOffSale(product)) {
                            product.updateStatus(ProductStatus.OFF_SHELF);
                            canUpdate = true;
                        } else {
                             log.warn("Product ID {} cannot be taken OFF_SHELF (status: {})", product.getId(), previousStatus);
                        }
                        break;
                    default:
                         log.warn("Unsupported target status {} for batch update on product ID {}", targetStatus, product.getId());
                        break;
                }
                if(canUpdate) {
                    successCount++;
                    log.debug("Updated status for product ID {} from {} to {}", product.getId(), previousStatus, targetStatus);
                } else {
                    log.warn("Status update skipped for product ID {} to target status {}", product.getId(), targetStatus);
                }
            } catch (Exception e) {
                log.error("Error updating status for product ID {}: {}", product.getId(), e.getMessage(), e);
            }
        }
        log.info("Batch status update finished. Successfully updated {} out of {} products.", successCount, productIds.size());
        return successCount;
    }
} 