package wu.platform.productManager.application.service;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.domain.vo.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 商品应用服务接口
 * 负责商品相关的用例协调和处理
 */
public interface ProductApplicationService {

    /**
     * 创建商品
     * 
     * @param command 创建商品命令
     * @return 创建后的商品DTO
     */
    ProductDto createProduct(CreateProductCommand command);

    /**
     * 查询商品详情
     * 
     * @param id 商品ID
     * @return 商品DTO
     */
    ProductDto getProduct(Long id);

    /**
     * 分页查询商品列表
     * 
     * @param categoryId 分类ID，可为null
     * @param merchantId 商家ID，可为null
     * @param status 商品状态，可为null
     * @param keyword 关键词，可为null
     * @param pageable 分页参数
     * @return 商品DTO分页结果
     */
    Page<ProductDto> queryProducts(Long categoryId, Long merchantId, ProductStatus status, 
                                  String keyword, Pageable pageable);

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 目标状态
     * @return 更新后的商品DTO
     */
    ProductDto updateProductStatus(Long id, ProductStatus status);

    /**
     * 批量更新商品状态
     * 
     * @param ids 商品ID列表
     * @param status 目标状态
     * @return 成功更新的商品数量
     */
    int batchUpdateProductStatus(List<Long> ids, ProductStatus status);

    /**
     * 删除商品
     * 
     * @param id 商品ID
     */
    void deleteProduct(Long id);
} 