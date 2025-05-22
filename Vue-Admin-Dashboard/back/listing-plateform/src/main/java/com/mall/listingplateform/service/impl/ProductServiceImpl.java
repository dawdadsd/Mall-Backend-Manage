package com.mall.listingplateform.service.impl;

import com.mall.listingplateform.dto.ProductDTO;
import com.mall.listingplateform.dto.ProductQueryParams;
import com.mall.listingplateform.exception.BusinessException;
import com.mall.listingplateform.model.Product;
import com.mall.listingplateform.model.Product.Status;
import com.mall.listingplateform.repository.ProductRepository;
import com.mall.listingplateform.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品服务实现类
 * 
 * 实现商品的核心业务逻辑，包括CRUD操作、状态管理、高级查询等功能
 * 采用函数式编程和Stream API提高代码可读性和性能
 * 
 * @since 2025
 * @version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    
    /**
     * 高级查询商品列表
     * 
     * 支持多条件组合查询，使用Specification实现动态查询条件构建
     * 
     * @param queryParams 查询参数封装对象
     * @param pageable 分页参数
     * @return 分页商品DTO列表
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(ProductQueryParams queryParams, Pageable pageable) {
        log.debug("开始查询商品列表，查询参数: {}", queryParams);
        
        Specification<Product> spec = buildSpecification(queryParams);
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }
    
    /**
     * 构建动态查询条件
     * 
     * @param params 查询参数
     * @return 动态查询条件
     */
    private Specification<Product> buildSpecification(ProductQueryParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 商品名称模糊查询
            if (StringUtils.hasText(params.getName())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    "%" + params.getName().toLowerCase() + "%"));
            }
            
            // 分类查询
            if (StringUtils.hasText(params.getCategoryId())) {
                predicates.add(criteriaBuilder.equal(
                    root.get("categoryId"), 
                    Long.valueOf(params.getCategoryId())));
            }
            
            // 状态查询
            if (StringUtils.hasText(params.getStatus())) {
                try {
                    predicates.add(criteriaBuilder.equal(
                        root.get("status"), 
                        Status.valueOf(params.getStatus())));
                } catch (IllegalArgumentException e) {
                    throw BusinessException.invalidParameter("无效的商品状态: " + params.getStatus());
                }
            }
            
            // 价格区间查询
            if (params.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("sellingPrice"), 
                    BigDecimal.valueOf(params.getMinPrice())));
            }
            
            if (params.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("sellingPrice"), 
                    BigDecimal.valueOf(params.getMaxPrice())));
            }
            
            // 商家ID查询
            if (params.getMerchantId() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("supplier").get("merchantId"), 
                    params.getMerchantId()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品DTO对象
     * @throws BusinessException 当商品不存在时抛出异常
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        log.debug("根据ID查询商品: {}", id);
        
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> BusinessException.resourceNotFound("商品", id));
    }

    /**
     * 保存商品信息
     * 
     * 支持创建新商品和更新现有商品
     * 更新操作会保留商品的创建时间和其他不应被修改的字段
     * 
     * @param productDTO 商品DTO对象
     * @return 保存后的商品DTO对象（包含生成的ID等信息）
     * @throws BusinessException 当商品编码重复或更新的商品不存在时抛出异常
     */
    @Override
    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("保存商品信息: {}", productDTO);
        
        // 参数校验
        validateProductDTO(productDTO);
        
        Product product;
        boolean isNewProduct = productDTO.getId() == null;
        
        if (isNewProduct) {
            // 创建新商品
            product = new Product();
            product.setCreateTime(LocalDateTime.now());
        } else {
            // 更新现有商品
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> BusinessException.resourceNotFound("商品", productDTO.getId()));
            
            // 检查状态流转是否合法
            validateStatusTransition(product.getStatus().name(), productDTO.getStatus());
        }
        
        // 校验商品编码唯一性
        if (StringUtils.hasText(productDTO.getCode()) && 
            (isNewProduct || !product.getCode().equals(productDTO.getCode())) && 
            productRepository.existsByCode(productDTO.getCode())) {
            
            throw BusinessException.resourceAlreadyExists("商品", "编码", productDTO.getCode());
        }
        
        // 更新商品属性
        updateProductFromDTO(product, productDTO);
        
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());
        
        // 保存商品
        Product savedProduct = productRepository.save(product);
        log.info("商品{}成功, ID: {}", isNewProduct ? "创建" : "更新", savedProduct.getId());
        
        return convertToDTO(savedProduct);
    }
    
    /**
     * 校验商品DTO数据
     * 
     * @param dto 商品DTO
     * @throws BusinessException 当校验失败时抛出异常
     */
    private void validateProductDTO(ProductDTO dto) {
        // 校验价格合理性
        if (dto.getSellingPrice() != null && dto.getSellingPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw BusinessException.invalidParameter("销售价格不能为负数");
        }
        
        if (dto.getCostPrice() != null && dto.getCostPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw BusinessException.invalidParameter("成本价格不能为负数");
        }
        
        // 校验库存合理性
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw BusinessException.invalidParameter("库存数量不能为负数");
        }
        
        // 校验商品状态
        if (StringUtils.hasText(dto.getStatus())) {
            try {
                Status.valueOf(dto.getStatus());
            } catch (IllegalArgumentException e) {
                throw BusinessException.invalidParameter("无效的商品状态: " + dto.getStatus());
            }
        }
    }
    
    /**
     * 校验商品状态流转是否合法
     * 
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @throws BusinessException 当状态流转不合法时抛出异常
     */
    private void validateStatusTransition(String currentStatus, String newStatus) {
        if (!StringUtils.hasText(newStatus) || currentStatus.equals(newStatus)) {
            return;
        }
        
        Status current = Status.valueOf(currentStatus);
        Status target = Status.valueOf(newStatus);
        
        // 定义状态流转规则
        switch (current) {
            case DRAFT:
                // 草稿可以转为待审核或删除
                if (target != Status.PENDING && target != Status.DELETED) {
                    throw BusinessException.operationNotAllowed(
                            String.format("商品状态不能从%s直接变更为%s", current, target));
                }
                break;
                
            case PENDING:
                // 待审核可以转为已审核、草稿或删除
                if (target != Status.APPROVED && target != Status.DRAFT && target != Status.DELETED) {
                    throw BusinessException.operationNotAllowed(
                            String.format("商品状态不能从%s直接变更为%s", current, target));
                }
                break;
                
            case APPROVED:
                // 已审核可以转为已上架、待审核或删除
                if (target != Status.PUBLISHED && target != Status.PENDING && target != Status.DELETED) {
                    throw BusinessException.operationNotAllowed(
                            String.format("商品状态不能从%s直接变更为%s", current, target));
                }
                break;
                
            case PUBLISHED:
                // 已上架只能转为已下架或删除
                if (target != Status.UNPUBLISHED && target != Status.DELETED) {
                    throw BusinessException.operationNotAllowed(
                            String.format("商品状态不能从%s直接变更为%s", current, target));
                }
                break;
                
            case UNPUBLISHED:
                // 已下架可以转为已上架、草稿或删除
                if (target != Status.PUBLISHED && target != Status.DRAFT && target != Status.DELETED) {
                    throw BusinessException.operationNotAllowed(
                            String.format("商品状态不能从%s直接变更为%s", current, target));
                }
                break;
                
            case DELETED:
                // 已删除不能转为其他状态
                throw BusinessException.operationNotAllowed("已删除的商品不能变更状态");
                
            default:
                break;
        }
    }
    
    /**
     * 根据DTO更新商品实体
     * 
     * @param product 商品实体
     * @param dto 商品DTO
     */
    private void updateProductFromDTO(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        product.setCategoryId(dto.getCategoryId());
        product.setBrandId(dto.getBrandId());
        product.setDescription(dto.getDescription());
        product.setDetailContent(dto.getDetailContent());
        product.setMainImage(dto.getMainImage());
        product.setImages(dto.getImages());
        product.setVideo(dto.getVideo());
        
        // 设置状态，如果有
        if (StringUtils.hasText(dto.getStatus())) {
            product.setStatus(Status.valueOf(dto.getStatus()));
        }
        
        // 设置价格
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setPromotionPrice(dto.getPromotionPrice());
        
        // 设置库存
        product.setStock(dto.getStock());
        product.setWarningStock(dto.getWarningStock());
        
        // 设置SEO信息
        product.setKeywords(dto.getKeywords());
        product.setSearchTags(dto.getSearchTags());
        
        // 设置规格和属性
        product.setSpecifications(dto.getSpecifications());
        product.setAttributes(dto.getAttributes());
        
        // 设置上下架时间
        if (Status.PUBLISHED.equals(product.getStatus()) && product.getPublishTime() == null) {
            product.setPublishTime(LocalDateTime.now());
        }
        
        if (Status.UNPUBLISHED.equals(product.getStatus()) && product.getUnpublishTime() == null) {
            product.setUnpublishTime(LocalDateTime.now());
        }
    }

    /**
     * 删除商品
     * 
     * 实际上是将商品状态设置为DELETED，不会真正从数据库删除
     * 
     * @param id 商品ID
     * @throws BusinessException 当商品不存在时抛出异常
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("删除商品，ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> BusinessException.resourceNotFound("商品", id));
        
        // 已经是删除状态，直接返回
        if (Status.DELETED.equals(product.getStatus())) {
            log.info("商品已经是删除状态，无需操作，ID: {}", id);
            return;
        }
        
        product.setStatus(Status.DELETED);
        product.setUpdateTime(LocalDateTime.now());
        
        productRepository.save(product);
        log.info("商品已标记为删除状态，ID: {}", id);
    }

    /**
     * 获取所有商品分类
     * 
     * @return 分类ID和名称映射
     */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, String> getALlCategories() {
        // 实际项目中应该从分类服务或分类表获取
        // 这里模拟返回一些静态分类
        Map<Long, String> categories = new HashMap<>();
        categories.put(1L, "电子产品");
        categories.put(2L, "服装");
        categories.put(3L, "家居");
        categories.put(4L, "食品");
        return categories;
    }

    /**
     * 获取所有商品状态
     * 
     * @return 状态和描述的映射
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getAllStatuses() {
        return Stream.of(Status.values())
                .collect(Collectors.toMap(
                    Status::name,
                    this::getStatusDescription
                ));
    }
    
    /**
     * 获取状态描述
     * 
     * @param status 状态枚举
     * @return 状态描述
     */
    private String getStatusDescription(Status status) {
        return switch (status) {
            case DRAFT -> "草稿";
            case PENDING -> "待审核";
            case APPROVED -> "已审核";
            case PUBLISHED -> "已上架";
            case UNPUBLISHED -> "已下架";
            case DELETED -> "已删除";
        };
    }
    
    /**
     * 将商品实体转换为DTO
     * 
     * @param product 商品实体
     * @return 商品DTO
     */
    private ProductDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .code(product.getCode())
                .brandId(product.getBrandId())
                .categoryId(product.getCategoryId())
                .description(product.getDescription())
                .detailContent(product.getDetailContent())
                .mainImage(product.getMainImage())
                .images(product.getImages())
                .video(product.getVideo())
                .status(product.getStatus().name())
                .publishTime(product.getPublishTime())
                .unpublishTime(product.getUnpublishTime())
                .originalPrice(product.getOriginalPrice())
                .sellingPrice(product.getSellingPrice())
                .costPrice(product.getCostPrice())
                .promotionPrice(product.getPromotionPrice())
                .stock(product.getStock())
                .warningStock(product.getWarningStock())
                .keywords(product.getKeywords())
                .searchTags(product.getSearchTags())
                .specifications(product.getSpecifications())
                .attributes(product.getAttributes())
                .createTime(product.getCreateTime())
                .updateTime(product.getUpdateTime())
                .build();
    }
}
