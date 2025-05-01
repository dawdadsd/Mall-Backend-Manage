package wu.platform.productManager.application.service;

import wu.platform.productManager.application.dto.command.CreateMerchantCommand;
import wu.platform.productManager.application.dto.query.MerchantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商家应用服务接口
 */
public interface MerchantApplicationService {
    
    /**
     * 创建商家
     * 
     * @param command 创建商家命令
     * @return 创建后的商家DTO
     */
    MerchantDto createMerchant(CreateMerchantCommand command);
    
    /**
     * 获取商家详情
     * 
     * @param id 商家ID
     * @return 商家DTO
     */
    MerchantDto getMerchant(Long id);
    
    /**
     * 获取所有商家
     * 
     * @param pageable 分页参数
     * @return 商家DTO分页结果
     */
    Page<MerchantDto> getAllMerchants(Pageable pageable);
    
    /**
     * 更新商家
     * 
     * @param id 商家ID
     * @param command 更新商家命令
     * @return 更新后的商家DTO
     */
    MerchantDto updateMerchant(Long id, CreateMerchantCommand command);
    
    /**
     * 更新商家信用评级
     *
     * @param id           商家ID
     * @param creditScore  信用分数
     * @return 更新后的商家DTO
     */
    MerchantDto updateMerchantCreditScore(Long id, Integer creditScore);
    
    /**
     * 删除商家
     * 
     * @param id 商家ID
     */
    void deleteMerchant(Long id);
} 