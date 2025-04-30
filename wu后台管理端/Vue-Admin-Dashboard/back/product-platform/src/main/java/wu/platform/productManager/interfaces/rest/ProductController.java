package wu.platform.productManager.interfaces.rest;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.application.service.ProductApplicationService;
import wu.platform.productManager.domain.vo.ProductStatus;
import wu.platform.productManager.interfaces.rest.request.BatchUpdateStatusRequest;
import wu.platform.productManager.interfaces.rest.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
/**
 * 商品REST控制器
 * 提供商品相关的RESTful API
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品管理相关的API")
public class ProductController {
    private final ProductApplicationService productApplicationService;
    /**
     * 创建商品
     * 
     * @param command 创建商品命令
     * @return 创建后的商品DTO
     */
    @PostMapping
    @Operation(summary = "创建商品", description = "创建新商品，并返回创建结果")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody CreateProductCommand command) {
        log.info("创建商品: {}", command.getName());
        ProductDto productDto = productApplicationService.createProduct(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productDto));
    }
    /**
     * 获取商品详情
     * 
     * @param id 商品ID
     * @return 商品DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品的详细信息")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        log.info("获取商品详情: {}", id);
        ProductDto productDto = productApplicationService.getProduct(id);
        return ResponseEntity.ok(ApiResponse.success(productDto));
    }

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
    @GetMapping
    @Operation(summary = "获取商品列表", description = "支持分页和多条件筛选")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> queryProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        
        log.info("分页查询商品列表: categoryId={}, merchantId={}, status={}, keyword={}", 
                categoryId, merchantId, status, keyword);
        
        Page<ProductDto> products = productApplicationService.queryProducts(
                categoryId, merchantId, status, keyword, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 目标状态
     * @return 更新后的商品DTO
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新商品状态", description = "更新商品的状态")
    public ResponseEntity<ApiResponse<ProductDto>> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        
        log.info("更新商品状态: id={}, status={}", id, status);
        ProductDto productDto = productApplicationService.updateProductStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(productDto));
    }

    /**
     * 批量更新商品状态
     * 
     * @param request 批量更新状态请求
     * @return 成功更新的商品数量
     */
    @PutMapping("/batch-status")
    @Operation(summary = "批量更新商品状态", description = "批量更新多个商品的状态")
    public ResponseEntity<ApiResponse<Integer>> batchUpdateProductStatus(
            @Valid @RequestBody BatchUpdateStatusRequest request) {
        
        log.info("批量更新商品状态: ids={}, status={}", request.getIds(), request.getStatus());
        
        int count = productApplicationService.batchUpdateProductStatus(
                request.getIds(), request.getStatus());
        
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 删除成功的响应
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("删除商品: {}", id);
        productApplicationService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
} 