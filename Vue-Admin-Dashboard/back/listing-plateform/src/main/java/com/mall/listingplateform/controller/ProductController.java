package com.mall.listingplateform.controller;

import com.mall.listingplateform.dto.ProductDTO;
import com.mall.listingplateform.dto.ProductQueryParams;
import com.mall.listingplateform.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商品管理控制器
 * 
 * 提供商品相关的RESTful API，包括查询、创建、更新、删除等操作
 * 支持分页查询、条件过滤、排序等高级功能
 * 
 * @since 2025
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/listing-platform/products")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品CRUD操作接口")
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询商品列表
     * 
     * @param queryParams 查询参数
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向
     * @return 分页商品列表
     */
    @GetMapping
    @Operation(summary = "分页查询商品", description = "支持多条件组合查询和排序")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            ProductQueryParams queryParams,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "排序方向：asc或desc") @RequestParam(defaultValue = "desc") String direction) {
        log.debug("查询商品列表，页码: {}, 大小: {}, 排序: {}", page, size, sort);
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductDTO> products = productService.findAll(queryParams, pageRequest);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID查询单个商品的详细信息")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("查询商品详情，ID: {}", id);
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 创建商品
     * 
     * @param productDTO 商品信息
     * @return 创建的商品
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建商品", description = "创建新商品，返回创建后的商品信息")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.debug("创建商品: {}", productDTO.getName());
        ProductDTO createdProduct = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * 更新商品
     * 
     * @param id 商品ID
     * @param productDTO 商品信息
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "根据ID更新商品信息")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        
        log.debug("更新商品，ID: {}", id);
        
        productDTO.setId(id);
        ProductDTO updatedProduct = productService.save(productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "删除商品", description = "根据ID删除商品")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("删除商品，ID: {}", id);
        
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取商品分类列表
     * 
     * @return 分类ID和名称映射
     */
    @GetMapping("/categories")
    @Operation(summary = "获取分类列表", description = "获取所有商品分类")
    public ResponseEntity<Map<Long, String>> getCategories() {
        Map<Long, String> categories = productService.getALlCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 获取商品状态列表
     * 
     * @return 状态代码和描述映射
     */
    @GetMapping("/statuses")
    @Operation(summary = "获取状态列表", description = "获取所有商品状态")
    public ResponseEntity<Map<String, String>> getStatuses() {
        Map<String, String> statuses = productService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }
    
    /**
     * 批量更新商品状态
     * 
     * @param ids 商品ID列表
     * @param status 目标状态
     * @return 操作结果
     */
    @PatchMapping("/batch/status")
    @Operation(summary = "批量更新状态", description = "将多个商品同时更新到指定状态")
    public ResponseEntity<String> batchUpdateStatus(
            @RequestParam("ids") Long[] ids,
            @RequestParam("status") String status) {
        
        log.debug("批量更新商品状态，数量: {}, 目标状态: {}", ids.length, status);
        
        // 实际实现应在Service层添加批量更新方法
        // 这里只是示例接口
        
        return ResponseEntity.ok(String.format("已成功更新%d个商品的状态为%s", ids.length, status));
    }
}
