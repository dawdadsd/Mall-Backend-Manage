package com.mall.merchant.dto;

import com.mall.merchant.model.enums.MerchantType;
import com.mall.merchant.model.enums.RatingLevel; // 假设评级信息也需要
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家基本信息 DTO
 * 用于在商品详情页或其他需要展示商家简略信息的场景
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantBasicInfoDTO {

    private Long merchantId; // 商家ID
    private String merchantName; // 商家名称
    private String avatarUrl; // 商家头像URL
    private MerchantType merchantType; // 商家类型
    private RatingLevel ratingLevel; // 商家信用评级 (可选)
    // 可以根据需要添加更多必要的基础信息，如店铺链接等

}
