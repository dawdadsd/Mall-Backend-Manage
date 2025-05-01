package wu.platform.productManager.application.mapper;

import wu.platform.productManager.application.dto.query.MerchantDto;
import wu.platform.productManager.domain.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 商家对象映射器
 * 使用MapStruct进行对象映射
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper {

    /**
     * 将商家实体转换为DTO
     * 
     * @param merchant 商家实体
     * @return 商家DTO
     */
    @Mapping(source = "verified", target = "isVerified")
    @Mapping(source = "enabled", target = "isEnabled")
    MerchantDto toDto(Merchant merchant);

    /**
     * 将商家实体列表转换为DTO列表
     * 
     * @param merchants 商家实体列表
     * @return 商家DTO列表
     */
    List<MerchantDto> toDtoList(List<Merchant> merchants);
} 