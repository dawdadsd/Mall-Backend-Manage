package wu.platform.productManager.application.service.impl;

import wu.platform.productManager.application.dto.credit.MerchantCreditSummaryDto;
import wu.platform.productManager.application.service.MerchantCreditService;
import wu.platform.productManager.common.exception.ResourceNotFoundException;
import wu.platform.productManager.domain.entity.Merchant;
import wu.platform.productManager.domain.repository.MerchantRepository;
import wu.platform.productManager.domain.vo.MerchantCreditLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * 商家信用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCreditServiceImpl implements MerchantCreditService {

    private final MerchantRepository merchantRepository;

    /**
     * 获取商家信用评级摘要
     *
     * @param merchantId 商家ID
     * @return 商家信用评级摘要
     */
    @Override
    @Transactional(readOnly = true)
    public MerchantCreditSummaryDto getMerchantCreditSummary(Long merchantId) {
        log.info("获取商家信用评级摘要: {}", merchantId);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        return mapToMerchantCreditSummary(merchant);
    }

    /**
     * 获取所有商家信用评级摘要（分页）
     *
     * @param pageable 分页参数
     * @return 分页的商家信用评级摘要
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MerchantCreditSummaryDto> getAllMerchantCreditSummaries(Pageable pageable) {
        log.info("获取所有商家信用评级摘要");
        
        Page<Merchant> merchantPage = merchantRepository.findAll(pageable);
        
        return merchantPage.map(this::mapToMerchantCreditSummary);
    }

    /**
     * 更新商家信用评级
     *
     * @param merchantId  商家ID
     * @param creditScore 新的信用分数
     * @return 更新后的商家信用评级摘要
     */
    @Override
    @Transactional
    public MerchantCreditSummaryDto updateMerchantCreditScore(Long merchantId, Integer creditScore) {
        log.info("更新商家信用评级: {}, 分数: {}", merchantId, creditScore);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        merchant.updateCreditScore(creditScore);
        merchant.setUpdatedAt(Instant.now());
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantCreditSummary(updatedMerchant);
    }

    /**
     * 更新商家交易成功率
     *
     * @param merchantId  商家ID
     * @param successRate 新的交易成功率
     * @return 更新后的商家信用评级摘要
     */
    @Override
    @Transactional
    public MerchantCreditSummaryDto updateTransactionSuccessRate(Long merchantId, Double successRate) {
        log.info("更新商家交易成功率: {}, 成功率: {}", merchantId, successRate);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        merchant.updateTransactionSuccessRate(successRate);
        merchant.setUpdatedAt(Instant.now());
        
        // 交易成功率变化可能影响综合信用分数
        calculateCreditScore(merchant);
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantCreditSummary(updatedMerchant);
    }

    /**
     * 更新商家客户满意度
     *
     * @param merchantId   商家ID
     * @param satisfaction 新的客户满意度
     * @return 更新后的商家信用评级摘要
     */
    @Override
    @Transactional
    public MerchantCreditSummaryDto updateCustomerSatisfaction(Long merchantId, Double satisfaction) {
        log.info("更新商家客户满意度: {}, 满意度: {}", merchantId, satisfaction);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        merchant.updateCustomerSatisfaction(satisfaction);
        merchant.setUpdatedAt(Instant.now());
        
        // 客户满意度变化可能影响综合信用分数
        calculateCreditScore(merchant);
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantCreditSummary(updatedMerchant);
    }

    /**
     * 更新商家售后服务质量评分
     *
     * @param merchantId   商家ID
     * @param serviceScore 新的售后服务质量评分
     * @return 更新后的商家信用评级摘要
     */
    @Override
    @Transactional
    public MerchantCreditSummaryDto updateAfterSalesService(Long merchantId, Double serviceScore) {
        log.info("更新商家售后服务质量评分: {}, 评分: {}", merchantId, serviceScore);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        merchant.updateAfterSalesService(serviceScore);
        merchant.setUpdatedAt(Instant.now());
        
        // 售后服务质量评分变化可能影响综合信用分数
        calculateCreditScore(merchant);
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantCreditSummary(updatedMerchant);
    }

    /**
     * 计算并更新商家综合信用分数
     * 根据交易成功率、客户满意度、售后服务评分等综合计算
     *
     * @param merchantId 商家ID
     * @return 更新后的商家信用评级摘要
     */
    @Override
    @Transactional
    public MerchantCreditSummaryDto calculateAndUpdateCreditScore(Long merchantId) {
        log.info("计算并更新商家综合信用分数: {}", merchantId);
        
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + merchantId));
        
        calculateCreditScore(merchant);
        merchant.setUpdatedAt(Instant.now());
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantCreditSummary(updatedMerchant);
    }

    /**
     * 计算商家信用分数
     * 根据交易成功率、客户满意度和售后服务质量评分综合计算
     *
     * @param merchant 商家实体
     */
    private void calculateCreditScore(Merchant merchant) {
        // 权重设置: 交易成功率40%，客户满意度30%，售后服务20%，原始评分10%
        double transactionWeight = 0.4;
        double satisfactionWeight = 0.3;
        double serviceWeight = 0.2;
        double ratingWeight = 0.1;
        
        // 归一化处理：将各项指标转换为0-100的分数
        // 交易成功率已经是0-100的范围
        double transactionScore = merchant.getTransactionSuccessRate() != null ? merchant.getTransactionSuccessRate() : 0;
        
        // 客户满意度是0-5的范围，转换为0-100
        double satisfactionScore = merchant.getCustomerSatisfaction() != null ? merchant.getCustomerSatisfaction() * 20 : 0;
        
        // 售后服务质量是0-5的范围，转换为0-100
        double serviceScore = merchant.getAfterSalesService() != null ? merchant.getAfterSalesService() * 20 : 0;
        
        // 原始评分是0-5的范围，转换为0-100
        double ratingScore = merchant.getRating() != null ? merchant.getRating() * 20 : 0;
        
        // 计算综合分数
        double totalScore = transactionScore * transactionWeight +
                            satisfactionScore * satisfactionWeight +
                            serviceScore * serviceWeight +
                            ratingScore * ratingWeight;
        
        // 四舍五入并限制在0-100范围内
        int creditScore = Math.min(100, Math.max(0, (int) Math.round(totalScore)));
        
        // 更新信用分数和等级
        merchant.updateCreditScore(creditScore);
    }

    /**
     * 将商家实体转换为信用评级摘要DTO
     *
     * @param merchant 商家实体
     * @return 商家信用评级摘要DTO
     */
    private MerchantCreditSummaryDto mapToMerchantCreditSummary(Merchant merchant) {
        MerchantCreditLevel creditLevelEnum = merchant.getCreditLevelEnum();
        
        return MerchantCreditSummaryDto.builder()
                .merchantId(merchant.getId())
                .merchantName(merchant.getName())
                .merchantCode(merchant.getCode())
                .creditLevel(merchant.getCreditLevel())
                .creditLevelDescription(creditLevelEnum.getDescription())
                .creditScore(merchant.getCreditScore())
                .transactionSuccessRate(merchant.getTransactionSuccessRate())
                .customerSatisfaction(merchant.getCustomerSatisfaction())
                .afterSalesService(merchant.getAfterSalesService())
                .isVerified(merchant.isVerified())
                .rating(merchant.getRating() != null ? merchant.getRating().floatValue() : null)
                .build();
    }
} 