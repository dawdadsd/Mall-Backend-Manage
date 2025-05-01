package wu.platform.productManager.interfaces.rest;

import wu.platform.productManager.application.dto.credit.MerchantCreditSummaryDto;
import wu.platform.productManager.application.service.MerchantCreditService;
import wu.platform.productManager.interfaces.rest.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 商家信用评级REST控制器
 * 提供商家信用评级相关的RESTful API
 */
@Tag(name = "商家信用评级", description = "商家信用评级相关操作")
@Slf4j
@RestController
@RequestMapping("/merchant-credits")
@RequiredArgsConstructor
public class MerchantCreditController {

    private final MerchantCreditService merchantCreditService;

    /**
     * 获取商家信用评级摘要
     *
     * @param merchantId 商家ID
     * @return 商家信用评级摘要
     */
    @Operation(summary = "获取商家信用评级摘要", description = "根据商家ID获取信用评级详情")
    @GetMapping("/{merchantId}")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> getMerchantCreditSummary(
            @PathVariable Long merchantId) {
        log.info("获取商家信用评级摘要请求: {}", merchantId);
        MerchantCreditSummaryDto summary = merchantCreditService.getMerchantCreditSummary(merchantId);
        return ResponseEntity.ok(ApiResponse.success("获取商家信用评级成功", summary));
    }

    /**
     * 获取所有商家信用评级摘要
     *
     * @param pageable 分页参数
     * @return 分页的商家信用评级摘要
     */
    @Operation(summary = "获取所有商家信用评级", description = "分页获取所有商家的信用评级摘要")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MerchantCreditSummaryDto>>> getAllMerchantCreditSummaries(
            Pageable pageable) {
        log.info("获取所有商家信用评级摘要请求");
        Page<MerchantCreditSummaryDto> page = merchantCreditService.getAllMerchantCreditSummaries(pageable);
        return ResponseEntity.ok(ApiResponse.success("获取所有商家信用评级成功", page));
    }

    /**
     * 更新商家信用分数
     *
     * @param merchantId  商家ID
     * @param creditScore 信用分数
     * @return 更新后的商家信用评级摘要
     */
    @Operation(summary = "更新商家信用分数", description = "手动更新商家的信用分数")
    @PutMapping("/{merchantId}/credit-score")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> updateCreditScore(
            @PathVariable Long merchantId,
            @RequestParam @Min(0) @Max(100) Integer creditScore) {
        log.info("更新商家信用分数请求: {}, 分数: {}", merchantId, creditScore);
        MerchantCreditSummaryDto summary = merchantCreditService.updateMerchantCreditScore(merchantId, creditScore);
        return ResponseEntity.ok(ApiResponse.success("商家信用分数更新成功", summary));
    }

    /**
     * 更新商家交易成功率
     *
     * @param merchantId  商家ID
     * @param successRate 交易成功率
     * @return 更新后的商家信用评级摘要
     */
    @Operation(summary = "更新商家交易成功率", description = "更新商家的交易成功率并重新计算信用分数")
    @PutMapping("/{merchantId}/transaction-success-rate")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> updateTransactionSuccessRate(
            @PathVariable Long merchantId,
            @RequestParam @Min(0) @Max(100) Double successRate) {
        log.info("更新商家交易成功率请求: {}, 成功率: {}", merchantId, successRate);
        MerchantCreditSummaryDto summary = merchantCreditService.updateTransactionSuccessRate(merchantId, successRate);
        return ResponseEntity.ok(ApiResponse.success("商家交易成功率更新成功", summary));
    }

    /**
     * 更新商家客户满意度
     *
     * @param merchantId   商家ID
     * @param satisfaction 客户满意度
     * @return 更新后的商家信用评级摘要
     */
    @Operation(summary = "更新商家客户满意度", description = "更新商家的客户满意度并重新计算信用分数")
    @PutMapping("/{merchantId}/customer-satisfaction")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> updateCustomerSatisfaction(
            @PathVariable Long merchantId,
            @RequestParam @Min(0) @Max(5) Double satisfaction) {
        log.info("更新商家客户满意度请求: {}, 满意度: {}", merchantId, satisfaction);
        MerchantCreditSummaryDto summary = merchantCreditService.updateCustomerSatisfaction(merchantId, satisfaction);
        return ResponseEntity.ok(ApiResponse.success("商家客户满意度更新成功", summary));
    }

    /**
     * 更新商家售后服务质量评分
     *
     * @param merchantId   商家ID
     * @param serviceScore 售后服务质量评分
     * @return 更新后的商家信用评级摘要
     */
    @Operation(summary = "更新商家售后服务质量评分", description = "更新商家的售后服务质量评分并重新计算信用分数")
    @PutMapping("/{merchantId}/after-sales-service")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> updateAfterSalesService(
            @PathVariable Long merchantId,
            @RequestParam @Min(0) @Max(5) Double serviceScore) {
        log.info("更新商家售后服务质量评分请求: {}, 评分: {}", merchantId, serviceScore);
        MerchantCreditSummaryDto summary = merchantCreditService.updateAfterSalesService(merchantId, serviceScore);
        return ResponseEntity.ok(ApiResponse.success("商家售后服务质量评分更新成功", summary));
    }

    /**
     * 计算并更新商家综合信用分数
     *
     * @param merchantId 商家ID
     * @return 更新后的商家信用评级摘要
     */
    @Operation(summary = "计算并更新商家综合信用分数", description = "根据各项指标重新计算商家的综合信用分数")
    @PostMapping("/{merchantId}/calculate")
    public ResponseEntity<ApiResponse<MerchantCreditSummaryDto>> calculateAndUpdateCreditScore(
            @PathVariable Long merchantId) {
        log.info("计算并更新商家综合信用分数请求: {}", merchantId);
        MerchantCreditSummaryDto summary = merchantCreditService.calculateAndUpdateCreditScore(merchantId);
        return ResponseEntity.ok(ApiResponse.success("商家综合信用分数计算更新成功", summary));
    }
} 