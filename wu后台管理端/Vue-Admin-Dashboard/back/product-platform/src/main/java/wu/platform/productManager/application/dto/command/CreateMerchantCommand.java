package wu.platform.productManager.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 创建商家命令
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantCommand {
    
    /**
     * 商家名称
     */
    @NotBlank(message = "商家名称不能为空")
    @Size(max = 100, message = "商家名称长度不能超过100个字符")
    private String name;
    
    /**
     * 商家编码
     */
    @NotBlank(message = "商家编码不能为空")
    @Size(max = 20, message = "商家编码长度不能超过20个字符")
    private String code;
    
    /**
     * 联系人
     */
    @Size(max = 50, message = "联系人姓名长度不能超过50个字符")
    private String contactPerson;
    
    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String contactPhone;
    
    /**
     * 电子邮箱
     */
    @Email(message = "电子邮箱格式不正确")
    @Size(max = 100, message = "电子邮箱长度不能超过100个字符")
    private String email;
    
    /**
     * 地址
     */
    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;
    
    /**
     * Logo URL
     */
    private String logoUrl;
    
    /**
     * 商家描述
     */
    @Size(max = 500, message = "商家描述长度不能超过500个字符")
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
    @Size(max = 1, message = "信用等级只能是单个字符")
    private String creditLevel;
    
    /**
     * 信用分数 (0-100)
     */
    @Min(value = 0, message = "信用分数不能小于0")
    @Max(value = 100, message = "信用分数不能大于100")
    private Integer creditScore;
    
    /**
     * 交易成功率
     */
    @Min(value = 0, message = "交易成功率不能小于0")
    @Max(value = 100, message = "交易成功率不能大于100")
    private Double transactionSuccessRate;
    
    /**
     * 客户满意度
     */
    @Min(value = 0, message = "客户满意度不能小于0")
    @Max(value = 5, message = "客户满意度不能大于5")
    private Double customerSatisfaction;
    
    /**
     * 售后服务质量评分
     */
    @Min(value = 0, message = "售后服务质量评分不能小于0")
    @Max(value = 5, message = "售后服务质量评分不能大于5")
    private Double afterSalesService;
    
    /**
     * 是否已验证
     */
    private Boolean isVerified;
    
    /**
     * 是否启用
     */
    private Boolean isEnabled;
} 