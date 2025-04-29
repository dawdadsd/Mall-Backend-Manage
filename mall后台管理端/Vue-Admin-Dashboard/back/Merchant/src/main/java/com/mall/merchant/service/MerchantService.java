package com.mall.merchant.service;

import com.mall.merchant.dto.MerchantBasicInfoDTO;
import com.mall.merchant.model.Merchant; // 假设内部或管理后台可能需要完整实体
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商家服务接口
 * 定义商家模块的核心业务逻辑方法
 */
public interface MerchantService {

    /**
     * 根据 ID 获取商家基本信息 (供外部服务如商品服务调用)
     *
     * @param merchantId 商家ID
     * @return 包含商家基本信息的 Optional DTO
     */
    Optional<MerchantBasicInfoDTO> getMerchantBasicInfoById(Long merchantId);

    /**
     * 根据 ID 列表批量获取商家基本信息 (优化跨服务调用性能)
     *
     * @param merchantIds 商家ID列表
     * @return 商家基本信息 DTO 列表
     */
    List<MerchantBasicInfoDTO> findMerchantBasicInfoByIds(List<Long> merchantIds);

    /**
     * 根据 ID 获取完整的商家信息（包含所有关联的扩展信息）
     * (通常用于商家模块内部或管理后台)
     *
     * @param merchantId 商家ID
     * @return 包含完整商家信息的 Optional 实体
     */
    Optional<Merchant> findFullMerchantById(Long merchantId);

    /**
     * 分页查询商家列表 (示例，可添加查询条件)
     *
     * @param pageable 分页参数
     * @return 商家分页数据 (可使用 MerchantBasicInfoDTO 或特定列表DTO)
     */
    Page<MerchantBasicInfoDTO> listMerchants(Pageable pageable);

    // --- 其他必要的商家管理服务方法 --- 
    // 例如: 
    // Merchant createMerchant(MerchantCreateDTO dto);
    // void updateMerchantStatus(Long merchantId, MerchantStatus status);
    // MerchantCertification processVerification(Long verificationId, boolean approved, String reason, Long adminId);
    // ...

} 