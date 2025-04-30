package com.mall.productplatform.domain.service;

import com.mall.productplatform.domain.entity.Product;
import com.mall.productplatform.domain.entity.ProductSku;
import com.mall.productplatform.domain.entity.ProductSpecification;
import com.mall.productplatform.domain.vo.ProductStatus;

import java.util.Set;

/**
 * 商品领域服务
 * 处理跨实体和聚合根的复杂业务逻辑
 */
public interface ProductDomainService {

    /**
     * 创建商品及其关联的规格和SKU
     * 
     * @param product 商品实体
     * @param specifications 规格列表
     * @param skus SKU列表
     * @return 创建后的商品实体
     */
    Product createProductWithSpecsAndSkus(Product product, Set<ProductSpecification> specifications, Set<ProductSku> skus);

    /**
     * 批量更新商品库存
     * 当SKU库存变更时，相应更新商品总库存
     * 
     * @param product 商品实体
     * @param skus 更新后的SKU列表
     * @return 更新后的商品实体
     */
    Product updateProductInventory(Product product, Set<ProductSku> skus);

    /**
     * 检查商品是否可以上架
     * 
     * @param product 商品实体
     * @return 是否可以上架
     */
    boolean canPutOnSale(Product product);

    /**
     * 检查商品是否可以下架
     * 
     * @param product 商品实体
     * @return 是否可以下架
     */
    boolean canTakeOffSale(Product product);

    /**
     * 批量变更商品状态
     * 
     * @param productIds 商品ID列表
     * @param targetStatus 目标状态
     * @return 变更成功的商品数量
     */
    int batchUpdateStatus(Set<Long> productIds, ProductStatus targetStatus);
} 