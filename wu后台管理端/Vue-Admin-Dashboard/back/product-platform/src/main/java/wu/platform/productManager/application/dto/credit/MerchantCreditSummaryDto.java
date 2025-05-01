package wu.platform.productManager.application.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家信用评级摘要DTO
 * 用于展示商家信用评级相关的摘要信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCreditSummaryDto {
    
    /**
     * 商家ID
     */
    private Long merchantId;
    
    /**
     * 商家名称
     */
    private String merchantName;
    
    /**
     * 商家编码
     */
    private String merchantCode;
    
    /**
     * 信用评级
     */
    private String creditLevel;
    
    /**
     * 信用等级描述
     */
    private String creditLevelDescription;
    
    /**
     * 信用分数
     */
    private Integer creditScore;
    
    /**
     * 交易成功率
     */
    private Double transactionSuccessRate;
    
    /**
     * 客户满意度
     */
    private Double customerSatisfaction;
    
    /**
     * 售后服务质量评分
     */
    private Double afterSalesService;
    
    /**
     * 是否已验证
     */
    private Boolean isVerified;
    
    /**
     * 评分
     */
    private Float rating;
} 