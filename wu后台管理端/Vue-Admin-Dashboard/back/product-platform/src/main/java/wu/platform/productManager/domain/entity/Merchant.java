package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 商家实体类
 * 提供商家（卖家）的基本信息
 */
@Entity
@Table(name = "merchants")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "contact_person")
    private String contactPerson;
    
    @Column(name = "contact_phone")
    private String contactPhone;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "logo_url")
    private String logoUrl;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "followers")
    private Integer followers;
    
    @Column(name = "rating")
    private Double rating;
    
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    
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
     * 验证商家
     */
    public void verify() {
        this.isVerified = true;
    }
    
    /**
     * 启用商家
     */
    public void enable() {
        this.isEnabled = true;
    }
    
    /**
     * 禁用商家
     */
    public void disable() {
        this.isEnabled = false;
    }
} 