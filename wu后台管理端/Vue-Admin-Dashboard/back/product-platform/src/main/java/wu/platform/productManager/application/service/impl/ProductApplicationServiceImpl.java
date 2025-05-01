package wu.platform.productManager.application.service.impl;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.command.CreateProductSkuCommand;
import wu.platform.productManager.application.dto.command.CreateProductSpecificationCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.application.mapper.ProductMapper;
import wu.platform.productManager.application.service.ProductApplicationService;
import wu.platform.productManager.domain.entity.*;
import wu.platform.productManager.domain.event.product.ProductCreatedEvent;
import wu.platform.productManager.domain.event.product.ProductStatusChangedEvent;
import wu.platform.productManager.domain.repository.CategoryRepository;
import wu.platform.productManager.domain.repository.MerchantRepository;
import wu.platform.productManager.domain.repository.ProductRepository;
import wu.platform.productManager.domain.service.ProductDomainService;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * 商品应用服务实现类
 * 负责商品相关的用例协调和处理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApplicationServiceImpl implements ProductApplicationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ProductDomainService productDomainService;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ProductDto createProduct(CreateProductCommand command) {
        log.info("Creating product with name: {}", command.getName());
        // 获取商品关联实体
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("分类不存在: " + command.getCategoryId()));
        
        Merchant merchant = merchantRepository.findById(command.getMerchantId())
                .orElseThrow(() -> new EntityNotFoundException("商家不存在: " + command.getMerchantId()));
        
        // 1. 创建商品基本信息 (使用Mapper)
        Product product = productMapper.toEntity(command);
        product.setCategory(category);
        product.setMerchant(merchant);
        
        // 2. 创建规格信息 (手动处理)
        if (command.getSpecifications() != null) {
            command.getSpecifications().forEach(specCommand -> {
                ProductSpecification spec = productMapper.specCommandToEntity(specCommand);
                // Manually add values for the specification
                specCommand.getValues().forEach(value -> {
                     spec.addValue(value);
                });
                product.addSpecification(spec); // Add spec to product
            });
        }

        // 3. 创建SKU信息 (使用Mapper + 手动设置关联)
        if (command.getSkus() != null) {
            command.getSkus().forEach(skuCommand -> {
                ProductSku sku = productMapper.skuCommandToEntity(skuCommand);
                product.addSku(sku); // Add sku to product, which sets the back-reference
            });
        }

        // 4. 创建图片信息 (手动处理)
        if (command.getImageUrls() != null) {
             int sortOrder = 0;
             for (String imageUrl : command.getImageUrls()) {
                 product.addImage(imageUrl, sortOrder++); // Use Product's business method
             }
        }
        
        // 5. 保存商品 (JPA Cascade should handle related entities)
        Product savedProduct = productRepository.save(product);
        log.info("Product saved with ID: {}", savedProduct.getId());
        
        // 6. 发布商品创建事件
        eventPublisher.publishEvent(new ProductCreatedEvent(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory().getId(),
                savedProduct.getMerchant().getId()
        ));
        
        // 7. 返回DTO
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> queryProducts(Long categoryId, Long merchantId, ProductStatus status, 
                                         String keyword, Pageable pageable) {
        log.info("Querying products with criteria - categoryId: {}, merchantId: {}, status: {}, keyword: {}", 
                 categoryId, merchantId, status, keyword);
        // Delegate complex query logic to repository or specification pattern if needed
        Page<Product> products = productRepository.findByCriteria(categoryId, merchantId, status, keyword, pageable);
        
        return products.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto updateProductStatus(Long id, ProductStatus status) {
        log.info("Updating status for product ID: {} to {}", id, status);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        ProductStatus previousStatus = product.getStatus();
        
        // Use domain method for status update
        product.updateStatus(status);
        
        Product savedProduct = productRepository.save(product);
        log.info("Product status updated for ID: {}", savedProduct.getId());
        
        // 发布状态变更事件
        eventPublisher.publishEvent(new ProductStatusChangedEvent(
                savedProduct.getId(),
                savedProduct.getName(),
                previousStatus,
                savedProduct.getStatus()
        ));
        
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public int batchUpdateProductStatus(List<Long> ids, ProductStatus status) { // Changed Set to List for common usage
        log.info("Batch updating status for product IDs: {} to {}", ids, status);
        List<Product> products = productRepository.findAllById(ids);
        int updatedCount = 0;
        for (Product product : products) {
             try {
                 ProductStatus previousStatus = product.getStatus();
                 product.updateStatus(status); 
                 productRepository.save(product); // Save each product
                 eventPublisher.publishEvent(new ProductStatusChangedEvent(
                     product.getId(),
                     product.getName(),
                     previousStatus,
                     product.getStatus()
                 ));
                 updatedCount++;
             } catch (Exception e) {
                 log.error("Failed to update status for product ID: {} to {}. Error: {}", product.getId(), status, e.getMessage());
                 // Decide on error handling: continue, rollback specific, rollback all?
             }
        }
        log.info("Batch status update completed. Updated {} products.", updatedCount);
        return updatedCount;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        productRepository.delete(product);
        log.info("Product deleted with ID: {}", id);
    }
} 