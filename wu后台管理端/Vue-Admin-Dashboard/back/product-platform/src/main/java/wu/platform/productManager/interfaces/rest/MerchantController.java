package wu.platform.productManager.interfaces.rest;

import wu.platform.productManager.application.dto.command.CreateMerchantCommand;
import wu.platform.productManager.application.dto.query.MerchantDto;
import wu.platform.productManager.application.service.MerchantApplicationService;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 商家REST控制器
 * 提供商家相关的RESTful API
 */
@Tag(name = "商家管理", description = "商家信息相关操作")
@Slf4j
@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantApplicationService merchantApplicationService;

    /**
     * 创建商家
     * 
     * @param command 创建商家命令
     * @return 创建的商家
     */
    @Operation(summary = "创建商家", description = "创建新的商家信息")
    @PostMapping
    public ResponseEntity<ApiResponse<MerchantDto>> createMerchant(@Valid @RequestBody CreateMerchantCommand command) {
        log.info("创建商家请求: {}", command.getName());
        MerchantDto merchantDto = merchantApplicationService.createMerchant(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("商家创建成功", merchantDto));
    }

    /**
     * 获取商家详情
     * 
     * @param id 商家ID
     * @return 商家信息
     */
    @Operation(summary = "获取商家详情", description = "根据ID获取商家详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MerchantDto>> getMerchant(@PathVariable Long id) {
        log.info("获取商家详情请求: {}", id);
        MerchantDto merchantDto = merchantApplicationService.getMerchant(id);
        return ResponseEntity.ok(ApiResponse.success("获取商家详情成功", merchantDto));
    }

    /**
     * 获取商家列表
     * 
     * @param pageable 分页参数
     * @return 商家列表
     */
    @Operation(summary = "获取商家列表", description = "分页获取商家列表")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MerchantDto>>> getAllMerchants(Pageable pageable) {
        log.info("获取商家列表请求");
        Page<MerchantDto> merchantsPage = merchantApplicationService.getAllMerchants(pageable);
        return ResponseEntity.ok(ApiResponse.success("获取商家列表成功", merchantsPage));
    }

    /**
     * 更新商家信息
     * 
     * @param id      商家ID
     * @param command 更新命令
     * @return 更新后的商家信息
     */
    @Operation(summary = "更新商家信息", description = "根据ID更新商家信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MerchantDto>> updateMerchant(
            @PathVariable Long id,
            @Valid @RequestBody CreateMerchantCommand command) {
        log.info("更新商家请求: {}", id);
        MerchantDto merchantDto = merchantApplicationService.updateMerchant(id, command);
        return ResponseEntity.ok(ApiResponse.success("商家更新成功", merchantDto));
    }
    
    /**
     * 更新商家信用评级
     *
     * @param id 商家ID
     * @param creditScore 信用分数
     * @return 更新后的商家信息
     */
    @Operation(summary = "更新商家信用评级", description = "根据ID更新商家信用评级分数")
    @PutMapping("/{id}/credit-score")
    public ResponseEntity<ApiResponse<MerchantDto>> updateMerchantCreditScore(
            @PathVariable Long id,
            @RequestParam @Min(0) @Max(100) Integer creditScore) {
        log.info("更新商家信用评级请求: {}, 分数: {}", id, creditScore);
        MerchantDto merchantDto = merchantApplicationService.updateMerchantCreditScore(id, creditScore);
        return ResponseEntity.ok(ApiResponse.success("商家信用评级更新成功", merchantDto));
    }

    /**
     * 删除商家
     * 
     * @param id 商家ID
     * @return 操作结果
     */
    @Operation(summary = "删除商家", description = "根据ID删除商家")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMerchant(@PathVariable Long id) {
        log.info("删除商家请求: {}", id);
        merchantApplicationService.deleteMerchant(id);
        return ResponseEntity.ok(ApiResponse.success("商家删除成功", null));
    }
} 