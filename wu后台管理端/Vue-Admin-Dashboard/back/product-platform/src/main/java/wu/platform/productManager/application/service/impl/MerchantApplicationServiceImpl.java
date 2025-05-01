package wu.platform.productManager.application.service.impl;

import wu.platform.productManager.application.dto.command.CreateMerchantCommand;
import wu.platform.productManager.application.dto.query.MerchantDto;
import wu.platform.productManager.application.service.MerchantApplicationService;
import wu.platform.productManager.common.exception.ResourceNotFoundException;
import wu.platform.productManager.domain.entity.Merchant;
import wu.platform.productManager.domain.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * 商家应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantApplicationServiceImpl implements MerchantApplicationService {

    private final MerchantRepository merchantRepository;

    /**
     * 创建商家
     *
     * @param command 创建商家命令
     * @return 创建后的商家DTO
     */
    @Override
    @Transactional
    public MerchantDto createMerchant(CreateMerchantCommand command) {
        log.info("创建商家: {}", command.getName());
        
        Merchant merchant = new Merchant();
        merchant.setName(command.getName());
        merchant.setCode(command.getCode());
        merchant.setContactPerson(command.getContactPerson());
        merchant.setContactPhone(command.getContactPhone());
        merchant.setEmail(command.getEmail());
        merchant.setAddress(command.getAddress());
        merchant.setLogoUrl(command.getLogoUrl());
        merchant.setDescription(command.getDescription());
        merchant.setFollowers(command.getFollowers());
        merchant.setRating(command.getRating() != null ? command.getRating().doubleValue() : 5.0);
        
        // 设置信用评级相关信息
        if(command.getCreditScore() != null) {
            merchant.updateCreditScore(command.getCreditScore());
        }
        if(command.getTransactionSuccessRate() != null) {
            merchant.updateTransactionSuccessRate(command.getTransactionSuccessRate());
        }
        if(command.getCustomerSatisfaction() != null) {
            merchant.updateCustomerSatisfaction(command.getCustomerSatisfaction());
        }
        if(command.getAfterSalesService() != null) {
            merchant.updateAfterSalesService(command.getAfterSalesService());
        }
        
        merchant.setVerified(command.getIsVerified() != null ? command.getIsVerified() : false);
        merchant.setEnabled(command.getIsEnabled() != null ? command.getIsEnabled() : true);
        merchant.setCreatedAt(Instant.now());
        merchant.setUpdatedAt(Instant.now());
        
        Merchant savedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantDto(savedMerchant);
    }

    /**
     * 获取商家详情
     *
     * @param id 商家ID
     * @return 商家DTO
     */
    @Override
    @Transactional(readOnly = true)
    public MerchantDto getMerchant(Long id) {
        log.info("获取商家: {}", id);
        
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + id));
        
        return mapToMerchantDto(merchant);
    }

    /**
     * 获取所有商家
     *
     * @param pageable 分页参数
     * @return 商家DTO分页结果
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MerchantDto> getAllMerchants(Pageable pageable) {
        log.info("获取所有商家");
        
        Page<Merchant> merchantPage = merchantRepository.findAll(pageable);
        
        return merchantPage.map(this::mapToMerchantDto);
    }

    /**
     * 更新商家
     *
     * @param id      商家ID
     * @param command 更新商家命令
     * @return 更新后的商家DTO
     */
    @Override
    @Transactional
    public MerchantDto updateMerchant(Long id, CreateMerchantCommand command) {
        log.info("更新商家: {}", id);
        
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + id));
        
        merchant.setName(command.getName());
        merchant.setCode(command.getCode());
        merchant.setContactPerson(command.getContactPerson());
        merchant.setContactPhone(command.getContactPhone());
        merchant.setEmail(command.getEmail());
        merchant.setAddress(command.getAddress());
        merchant.setLogoUrl(command.getLogoUrl());
        merchant.setDescription(command.getDescription());
        merchant.setFollowers(command.getFollowers());
        merchant.setRating(command.getRating() != null ? command.getRating().doubleValue() : merchant.getRating());
        
        // 更新信用评级相关信息
        if(command.getCreditScore() != null) {
            merchant.updateCreditScore(command.getCreditScore());
        }
        if(command.getTransactionSuccessRate() != null) {
            merchant.updateTransactionSuccessRate(command.getTransactionSuccessRate());
        }
        if(command.getCustomerSatisfaction() != null) {
            merchant.updateCustomerSatisfaction(command.getCustomerSatisfaction());
        }
        if(command.getAfterSalesService() != null) {
            merchant.updateAfterSalesService(command.getAfterSalesService());
        }
        
        merchant.setVerified(command.getIsVerified() != null ? command.getIsVerified() : merchant.isVerified());
        merchant.setEnabled(command.getIsEnabled() != null ? command.getIsEnabled() : merchant.isEnabled());
        merchant.setUpdatedAt(Instant.now());
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantDto(updatedMerchant);
    }

    /**
     * 更新商家信用评级
     *
     * @param id           商家ID
     * @param creditScore  信用分数
     * @return 更新后的商家DTO
     */
    @Override
    @Transactional
    public MerchantDto updateMerchantCreditScore(Long id, Integer creditScore) {
        log.info("更新商家信用评级: {}, 分数: {}", id, creditScore);
        
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + id));
        
        merchant.updateCreditScore(creditScore);
        merchant.setUpdatedAt(Instant.now());
        
        Merchant updatedMerchant = merchantRepository.save(merchant);
        
        return mapToMerchantDto(updatedMerchant);
    }

    /**
     * 删除商家
     *
     * @param id 商家ID
     */
    @Override
    @Transactional
    public void deleteMerchant(Long id) {
        log.info("删除商家: {}", id);
        
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商家不存在: " + id));
        
        merchantRepository.delete(merchant);
    }

    /**
     * 将实体映射为DTO
     *
     * @param merchant 商家实体
     * @return 商家DTO
     */
    private MerchantDto mapToMerchantDto(Merchant merchant) {
        return MerchantDto.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .code(merchant.getCode())
                .contactPerson(merchant.getContactPerson())
                .contactPhone(merchant.getContactPhone())
                .email(merchant.getEmail())
                .address(merchant.getAddress())
                .logoUrl(merchant.getLogoUrl())
                .description(merchant.getDescription())
                .followers(merchant.getFollowers())
                .rating(merchant.getRating().floatValue())
                .creditLevel(merchant.getCreditLevel())
                .creditScore(merchant.getCreditScore())
                .transactionSuccessRate(merchant.getTransactionSuccessRate())
                .customerSatisfaction(merchant.getCustomerSatisfaction())
                .afterSalesService(merchant.getAfterSalesService())
                .isVerified(merchant.isVerified())
                .isEnabled(merchant.isEnabled())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .build();
    }
} 