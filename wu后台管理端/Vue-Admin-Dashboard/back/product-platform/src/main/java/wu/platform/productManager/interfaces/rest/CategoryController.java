package wu.platform.productManager.interfaces.rest;

import wu.platform.productManager.application.dto.command.CreateCategoryCommand;
import wu.platform.productManager.application.dto.query.CategoryDto;
import wu.platform.productManager.application.service.CategoryApplicationService;
import wu.platform.productManager.interfaces.rest.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 商品分类REST控制器
 * 提供商品分类相关的RESTful API
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "商品分类管理", description = "商品分类管理相关的API")
public class CategoryController {
    private final CategoryApplicationService categoryApplicationService;

    /**
     * 创建商品分类
     * 
     * @param command 创建商品分类命令
     * @return 创建后的商品分类DTO
     */
    @PostMapping
    @Operation(summary = "创建商品分类", description = "创建新商品分类，并返回创建结果")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CreateCategoryCommand command) {
        log.info("创建商品分类: {}", command.getName());
        CategoryDto categoryDto = categoryApplicationService.createCategory(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryDto));
    }

    /**
     * 获取商品分类详情
     * 
     * @param id 商品分类ID
     * @return 商品分类DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取商品分类详情", description = "根据ID获取商品分类的详细信息")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(@PathVariable Long id) {
        log.info("获取商品分类详情: {}", id);
        CategoryDto categoryDto = categoryApplicationService.getCategory(id);
        return ResponseEntity.ok(ApiResponse.success(categoryDto));
    }

    /**
     * 获取所有商品分类
     * 
     * @param pageable 分页参数
     * @return 商品分类DTO分页结果
     */
    @GetMapping
    @Operation(summary = "获取商品分类列表", description = "支持分页查询商品分类")
    public ResponseEntity<ApiResponse<Page<CategoryDto>>> getAllCategories(Pageable pageable) {
        log.info("获取商品分类列表");
        Page<CategoryDto> categories = categoryApplicationService.getAllCategories(pageable);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * 更新商品分类
     * 
     * @param id 商品分类ID
     * @param command 更新商品分类命令
     * @return 更新后的商品分类DTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新商品分类", description = "更新商品分类信息")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryCommand command) {
        
        log.info("更新商品分类: id={}", id);
        CategoryDto categoryDto = categoryApplicationService.updateCategory(id, command);
        return ResponseEntity.ok(ApiResponse.success(categoryDto));
    }

    /**
     * 删除商品分类
     * 
     * @param id 商品分类ID
     * @return 删除成功的响应
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品分类", description = "根据ID删除商品分类")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        log.info("删除商品分类: {}", id);
        categoryApplicationService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
} 