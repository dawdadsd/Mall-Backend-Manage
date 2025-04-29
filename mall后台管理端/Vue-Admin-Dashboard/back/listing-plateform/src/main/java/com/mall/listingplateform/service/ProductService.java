package com.mall.listingplateform.service;

import com.mall.listingplateform.dto.ProductBriefDTO;
import com.mall.listingplateform.dto.ProductCreateDTO;
import com.mall.listingplateform.dto.ProductDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 商品服务接口
 * 定义商品模块的核心业务逻辑方法，包括与商家模块的交互
 */
public interface ProductService {

    /**
     * 根据 ID 获取商品详细信息
     * (实现中需要调用 MerchantService 获取商家信息并填充到 DTO 中)
     *
     * @param productId 商品ID
     * @return 包含商品详细信息的 Optional DTO
     */
    Optional<ProductDetailDTO> getProductDetailById(Long productId);

    /**
     * 根据商家 ID 分页查询该商家的商品列表
     *
     * @param merchantId 商家ID
     * @param pageable   分页参数
     * @return 商品简略信息分页 DTO
     */
    Page<ProductBriefDTO> findProductsByMerchantId(Long merchantId, Pageable pageable);

    /**
     * 创建新商品
     * (实现中需要校验 merchantId 是否有效，例如通过调用 MerchantService)
     *
     * @param createDTO 商品创建数据
     * @return 创建成功后的商品简略信息 DTO
     */
    ProductBriefDTO createProduct(ProductCreateDTO createDTO);

    /**
     * 根据商品 ID 更新商品信息
     *
     * @param productId 商品ID
     * @param updateDTO 商品更新数据
     * @return 更新后的商品详细信息 DTO
     */
    Optional<ProductDetailDTO> updateProduct(Long productId, /* ProductUpdateDTO */ Object updateDTO); // 建议定义 ProductUpdateDTO

    /**
     * 根据商品 ID 删除商品
     *
     * @param productId 商品ID
     */
    void deleteProduct(Long productId);

    /**
     * 更改商品状态 (上架/下架等)
     *
     * @param productId 商品ID
     * @param status    新的状态 (建议使用枚举)
     * @return 更新状态后的商品简略信息 DTO
     */
    Optional<ProductBriefDTO> updateProductStatus(Long productId, String status);

    // --- 其他可能的商品服务方法 ---
    // 例如：搜索商品, 获取分类/品牌下的商品等
    // Page<ProductBriefDTO> searchProducts(String keyword, Pageable pageable);
    // Page<ProductBriefDTO> findProductsByCategory(Long categoryId, Pageable pageable);

}
