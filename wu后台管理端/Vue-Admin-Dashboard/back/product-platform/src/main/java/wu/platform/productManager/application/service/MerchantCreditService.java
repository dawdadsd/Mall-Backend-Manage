package wu.platform.productManager.application.service;

import wu.platform.productManager.application.dto.credit.MerchantCreditSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商家信用服务接口
 * 提供商家信用评级相关的服务
 */
public interface MerchantCreditService {
    
    /**
     * 获取商家信用评级摘要
     * 
     * @param merchantId 商家ID
     * @return 商家信用评级摘要
     */
    MerchantCreditSummaryDto getMerchantCreditSummary(Long merchantId);
    
    /**
     * 获取所有商家信用评级摘要（分页）
     * 
     * @param pageable 分页参数
     * @return 分页的商家信用评级摘要
     */
    Page<MerchantCreditSummaryDto> getAllMerchantCreditSummaries(Pageable pageable);
    
    /**
     * 更新商家信用评级
     * 
     * @param merchantId 商家ID
     * @param creditScore 新的信用分数
     * @return 更新后的商家信用评级摘要
     */
    MerchantCreditSummaryDto updateMerchantCreditScore(Long merchantId, Integer creditScore);
    
    /**
     * 更新商家交易成功率
     * 
     * @param merchantId 商家ID
     * @param successRate 新的交易成功率
     * @return 更新后的商家信用评级摘要
     */
    MerchantCreditSummaryDto updateTransactionSuccessRate(Long merchantId, Double successRate);
    
    /**
     * 更新商家客户满意度
     * 
     * @param merchantId 商家ID
     * @param satisfaction 新的客户满意度
     * @return 更新后的商家信用评级摘要
     */
    MerchantCreditSummaryDto updateCustomerSatisfaction(Long merchantId, Double satisfaction);
    
    /**
     * 更新商家售后服务质量评分
     * 
     * @param merchantId 商家ID
     * @param serviceScore 新的售后服务质量评分
     * @return 更新后的商家信用评级摘要
     */
    MerchantCreditSummaryDto updateAfterSalesService(Long merchantId, Double serviceScore);
    
    /**
     * 计算并更新商家综合信用分数
     * 根据交易成功率、客户满意度、售后服务评分等综合计算
     * 
     * @param merchantId 商家ID
     * @return 更新后的商家信用评级摘要
     */
    MerchantCreditSummaryDto calculateAndUpdateCreditScore(Long merchantId);
} 