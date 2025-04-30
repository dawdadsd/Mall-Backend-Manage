package wu.platform.productManager.application.service.impl;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.application.dto.specification.SpecificationDto;
import wu.platform.productManager.application.mapper.ProductMapper;
import wu.platform.productManager.application.service.ProductApplicationService;
import com.mall.productplatform.domain.entity.*;
import wu.platform.productManager.domain.entity.*;
import wu.platform.productManager.domain.event.product.ProductCreatedEvent;
import wu.platform.productManager.domain.event.product.ProductStatusChangedEvent;
import wu.platform.productManager.domain.repository.CategoryRepository;
import wu.platform.productManager.domain.repository.MerchantRepository;
import wu.platform.productManager.domain.repository.ProductRepository;
import wu.platform.productManager.domain.service.ProductDomainService;
import wu.platform.productManager.domain.vo.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品应用服务实现类
 * 负责商品相关的用例协调和处理
 */
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
        // 获取商品关联实体
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("分类不存在: " + command.getCategoryId()));
        
        Merchant merchant = merchantRepository.findById(command.getMerchantId())
                .orElseThrow(() -> new EntityNotFoundException("商家不存在: " + command.getMerchantId()));
        
        // 创建商品基本信息
        Product product = productMapper.toEntity(command);
        product.setCategory(category);
        product.setMerchant(merchant);
        
        // 创建规格信息
        Set<ProductSpecification> specifications = new HashSet<>();
        for (SpecificationDto specDto : command.getSpecifications()) {
            for (String value : specDto.getValues()) {
                ProductSpecification spec = new ProductSpecification();
                spec.setName(specDto.getName());
                spec.setValues(value);
                specifications.add(spec);
            }
        }
        
        // 创建SKU信息
        Set<ProductSku> skus = command.getSkus().stream()
                .map(productMapper::toEntity)
                .collect(Collectors.toSet());
        
        // 创建图片信息
        for (String imageUrl : command.getImageUrls()) {
            ProductImage image = new ProductImage();
            image.setUrl(imageUrl);
            image.setSortOrder(command.getImageUrls().indexOf(imageUrl));
            product.addImage(image);
        }
        
        // 使用领域服务创建完整商品
        Product savedProduct = productDomainService.createProductWithSpecsAndSkus(product, specifications, skus);
        
        // 发布商品创建事件
        eventPublisher.publishEvent(new ProductCreatedEvent(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory().getId(),
                savedProduct.getMerchant().getId()
        ));
        
        // 返回DTO
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> queryProducts(Long categoryId, Long merchantId, ProductStatus status, 
                                         String keyword, Pageable pageable) {
        // 此处需要实现复杂的查询逻辑，可能需要在Repository中添加自定义查询方法
        // 为简化示例，先使用基本查询
        Page<Product> products;
        
        if (status != null) {
            products = productRepository.findByStatus(status, pageable);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else if (merchantId != null) {
            products = productRepository.findByMerchantId(merchantId, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContaining(keyword, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        
        return products.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto updateProductStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        ProductStatus previousStatus = product.getStatus();
        
        switch (status) {
            case ON_SALE:
                if (productDomainService.canPutOnSale(product)) {
                    product.putOnSale();
                } else {
                    throw new IllegalStateException("商品 [" + id + "] 不能上架，请检查库存和其他条件");
                }
                break;
            case OFF_SHELF:
                if (productDomainService.canTakeOffSale(product)) {
                    product.takeOffSale();
                } else {
                    throw new IllegalStateException("商品 [" + id + "] 不能下架");
                }
                break;
            // 可以添加其他状态的处理逻辑
            default:
                throw new IllegalArgumentException("不支持的商品状态: " + status);
        }
        
        Product savedProduct = productRepository.save(product);
        
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
    public int batchUpdateProductStatus(Set<Long> ids, ProductStatus status) {
        return productDomainService.batchUpdateStatus(ids, status);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在: " + id));
        
        productRepository.delete(product);
    }
} 