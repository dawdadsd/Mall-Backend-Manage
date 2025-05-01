package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import wu.platform.productManager.domain.vo.MerchantCreditLevel;
import java.time.Instant;

/**
 * 商家实体类
 * 提供商家（卖家）的基本信息
 */
@Entity
@Table(name = "merchants")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Merchant extends BaseEntity {

    /**
     * 商家名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * 商家编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;
    
    /**
     * 联系人
     */
    @Column(name = "contact_person", length = 50)
    private String contactPerson;
    
    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;
    
    /**
     * 电子邮箱
     */
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * 地址
     */
    @Column(name = "address", length = 200)
    private String address;
    
    /**
     * Logo URL
     */
    @Column(name = "logo_url")
    private String logoUrl;
    
    /**
     * 商家描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 关注者数量
     */
    @Column(name = "followers")
    private Integer followers = 0;
    
    /**
     * 评分
     */
    @Column(name = "rating")
    private Double rating = 5.0;
    
    /**
     * 信用等级 (A, B, C, D, E)
     */
    @Column(name = "credit_level", length = 1)
    private String creditLevel = MerchantCreditLevel.C.name();
    
    /**
     * 信用分数 (0-100)
     */
    @Column(name = "credit_score")
    private Integer creditScore = 60;
    
    /**
     * 交易成功率
     */
    @Column(name = "transaction_success_rate")
    private Double transactionSuccessRate = 0.0;
    
    /**
     * 客户满意度
     */
    @Column(name = "customer_satisfaction")
    private Double customerSatisfaction = 0.0;
    
    /**
     * 售后服务质量评分
     */
    @Column(name = "after_sales_service")
    private Double afterSalesService = 0.0;
    
    /**
     * 是否已验证
     */
    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;
    
    /**
     * 是否启用
     */
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;
    
    /**
     * 增加粉丝数
     */
    public void increaseFollowers(int count){
        if(count < 0)
        {
            throw new IllegalArgumentException("粉丝必须大于0");
        }
        this.followers = (this.followers != null ? this.followers : 0) +count;
    }
    
    /**
     * 减少粉丝数
     */
    public void decreaseFollowers(int count){
        if(count <= 0)
        {
            throw new IllegalArgumentException("粉丝数不能小于0");
        }
        int currentFollowers = this.followers != null ? this.followers : 0;
        this.followers = Math.max(0,currentFollowers - count);
    }
    
    /**
     * 更新评分
     */
    public void updateRating(double newRating){
        if(newRating < 0 || newRating > 5)
        {
            throw new IllegalArgumentException("商家评分需要在0-5区间选择");
        }
        this.rating = newRating;
    }
    
    /**
     * 更新信用评级
     * 根据信用分数自动计算信用等级
     */
    public void updateCreditScore(int creditScore) {
        if(creditScore < 0 || creditScore > 100) {
            throw new IllegalArgumentException("信用分数需要在0-100区间内");
        }
        
        this.creditScore = creditScore;
        MerchantCreditLevel level = MerchantCreditLevel.fromScore(creditScore);
        this.creditLevel = level.name();
    }
    
    /**
     * 获取当前信用等级枚举
     * 
     * @return 信用等级枚举
     */
    @Transient
    public MerchantCreditLevel getCreditLevelEnum() {
        return MerchantCreditLevel.valueOf(this.creditLevel);
    }
    
    /**
     * 更新交易成功率
     */
    public void updateTransactionSuccessRate(double rate) {
        if(rate < 0 || rate > 100) {
            throw new IllegalArgumentException("交易成功率需要在0-100区间内");
        }
        this.transactionSuccessRate = rate;
    }
    
    /**
     * 更新客户满意度
     */
    public void updateCustomerSatisfaction(double satisfaction) {
        if(satisfaction < 0 || satisfaction > 5) {
            throw new IllegalArgumentException("客户满意度需要在0-5区间内");
        }
        this.customerSatisfaction = satisfaction;
    }
    
    /**
     * 更新售后服务质量评分
     */
    public void updateAfterSalesService(double score) {
        if(score < 0 || score > 5) {
            throw new IllegalArgumentException("售后服务质量评分需要在0-5区间内");
        }
        this.afterSalesService = score;
    }
    
    /**
     * 验证商家
     */
    public void verify() {
        this.verified = true;
    }
    
    /**
     * 启用商家
     */
    public void enable() {
        this.enabled = true;
    }
    
    /**
     * 禁用商家
     */
    public void disable() {
        this.enabled = false;
    }
} 