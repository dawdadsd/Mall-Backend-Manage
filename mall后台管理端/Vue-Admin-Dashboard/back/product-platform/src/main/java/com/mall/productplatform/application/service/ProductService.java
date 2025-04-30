package com.mall.productplatform.application.service;

import com.mall.productplatform.application.dto.*;
import com.mall.productplatform.domain.vo.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品服务接口
 * 定义商品的所有业务操作
 */
@Service
public interface ProductService {

    /**
     * 获取商品列表（支持分页和筛选）
     */
    Page<ProductDTO> findProducts(String category, String status, Integer minFollowers, 
                                Integer maxFollowers, BigDecimal minPrice, BigDecimal maxPrice, 
                                int page, int size);
    
    /**
     * 获取商品详情
     */
    ProductDetailDTO findById(Long id);
    
    /**
     * 创建商品
     */
    ProductDTO createProduct(ProductCreateDTO productDTO);
    
    /**
     * 更新商品
     */
    ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO);
    
    /**
     * 删除商品
     */
    void deleteProduct(Long id);
    
    /**
     * 批量更新商品状态
     */
    void batchUpdateStatus(List<Long> productIds, ProductStatus status);
    
    /**
     * 批量删除商品
     */
    void batchDeleteProducts(List<Long> productIds);
    
    /**
     * 上传商品图片
     */
    List<String> uploadImages(Long productId, List<MultipartFile> files);
    
    /**
     * 获取商品分类列表
     */
    List<CategoryDTO> findAllCategories();
    
    /**
     * 按分类查询商品
     */
    Page<ProductDTO> findProductsByCategory(Long categoryId, int page, int size);
    
    /**
     * 按状态查询商品
     */
    Page<ProductDTO> findProductsByStatus(ProductStatus status, int page, int size);
    
    /**
     * 按商家查询商品
     */
    Page<ProductDTO> findProductsByMerchant(Long merchantId, int page, int size);
    
    /**
     * 关键字搜索商品
     */
    Page<ProductDTO> searchProducts(String keyword, int page, int size);
    
    /**
     * 获取热销商品
     */
    List<ProductDTO> findHotSellingProducts(int limit);
    
    /**
     * 获取新增商品
     */
    List<ProductDTO> findNewlyAddedProducts(int limit);
    
    /**
     * 获取低库存商品
     */
    List<ProductDTO> findLowInventoryProducts(int threshold);
    
    /**
     * 更新商品品相
     */
    ProductDTO updateProductCondition(Long id, ProductConditionUpdateDTO dto);
    
    /**
     * 获取商品SKU列表
     */
    List<ProductDetailDTO.ProductSkuDTO> findProductSkus(Long productId);
    
    /**
     * 更新SKU库存
     */
    void updateSkuInventory(Long skuId, Integer newInventory);
    
    /**
     * 批量更新SKU库存
     */
    void batchUpdateSkuInventory(List<SkuInventoryUpdateDTO> updates);
    
    /**
     * 商品销售记录
     */
    void recordProductSale(Long productId, Long skuId, Integer quantity);
} 