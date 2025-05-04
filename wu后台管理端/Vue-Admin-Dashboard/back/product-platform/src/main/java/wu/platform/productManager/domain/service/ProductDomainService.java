package wu.platform.productManager.domain.service;

import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.entity.ProductSku;
import wu.platform.productManager.domain.entity.ProductSpecification;
import wu.platform.productManager.domain.vo.ProductStatus;
import java.util.List;
import java.util.Set;

/**
 * 商品领域服务
 * 处理跨实体和聚合根的复杂业务逻辑
 */
public interface ProductDomainService {

    /**
     * 创建商品及其关联的规格和SKU
     * @param product 商品实体
     * @param specifications 规格列表
     * @param skus SKU列表
     * @return 创建后的商品实体
     */
    Product createProductWithSpecsAndSkus(Product product, List<ProductSpecification> specifications, List<ProductSku> skus);

    /**
     * 批量更新商品库存
     * 当SKU库存变更时，相应更新商品总库存
     * 
     * @param product 商品实体
     * @param skus 更新后的SKU列表
     * @return 更新后的商品实体
     */
    Product updateProductInventory(Product product, List<ProductSku> skus);

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
    int batchUpdateStatus(List<Long> productIds, ProductStatus targetStatus);
} 