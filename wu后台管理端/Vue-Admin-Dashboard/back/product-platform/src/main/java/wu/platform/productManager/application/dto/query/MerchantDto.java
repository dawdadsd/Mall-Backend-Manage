package wu.platform.productManager.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

/**
 * 商家DTO
 * 用于传输商家信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {

    /**
     * 商家ID
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家编码
     */
    private String code;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * Logo URL
     */
    private String logoUrl;

    /**
     * 商家描述
     */
    private String description;

    /**
     * 关注者数量
     */
    private Integer followers;

    /**
     * 评分
     */
    private Float rating;

    /**
     * 信用等级 (A, B, C, D, E)
     */
    private String creditLevel;
    
    /**
     * 信用分数 (0-100)
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
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 创建时间
     */
    private Instant createdAt;

    /**
     * 更新时间
     */
    private Instant updatedAt;
} 