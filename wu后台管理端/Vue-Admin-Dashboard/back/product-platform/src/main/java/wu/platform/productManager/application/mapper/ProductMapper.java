package wu.platform.productManager.application.mapper;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.command.CreateProductSkuCommand;
import wu.platform.productManager.application.dto.command.CreateProductSpecificationCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.application.dto.query.ProductSkuDto;
import wu.platform.productManager.application.dto.query.SpecificationViewDto;
import org.mapstruct.*;
import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.entity.ProductImage;
import wu.platform.productManager.domain.entity.ProductSku;
import wu.platform.productManager.domain.entity.ProductSpecification;
import wu.platform.productManager.domain.entity.*;
import java.util.*;
import java.util.stream.Collectors;
import wu.platform.productManager.application.dto.query.SpecificationViewDto.SpecValueDto;

/**
 * 商品对象映射器
 * 使用MapStruct进行对象映射
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, MerchantMapper.class})
public interface ProductMapper {

    /**
     * 将商品实体转换为DTO
     * 
     * @param product 商品实体
     * @return 商品DTO
     */
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImagesToUrls")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "mapSpecificationsToViewDtos")
    @Mapping(target = "skus", source = "skus", qualifiedByName = "mapSkusToDtos")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "merchant", source = "merchant") 
    ProductDto toDto(Product product);

    /**
     * 将创建商品命令转换为商品实体
     * 
     * @param command 创建商品命令
     * @return 商品实体
     */
    @BeanMapping(ignoreByDefault = true) // Ignore unmapped fields, including BaseEntity fields
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "condition", source = "condition")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "tags", source = "tags")
    // category, merchant, images, attributes, specifications, skus are ignored (handled in service)
    Product toEntity(CreateProductCommand command);

    /**
     * 将SKU命令转换为SKU实体
     * 
     * @param command SKU命令
     * @return SKU实体
     */
    @BeanMapping(ignoreByDefault = true) // Ignore unmapped fields, including BaseEntity fields
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "inventory", source = "inventory")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "specifications", source = "specifications")
    @Mapping(target = "enabled", source = "enabled") 
    @Mapping(target = "sales", constant = "0") // Default sales to 0
    // product is ignored (set when added to Product)
    ProductSku skuCommandToEntity(CreateProductSkuCommand command);

    /**
     * 将规格命令转换为规格实体
     *
     * @param command 规格命令
     * @return 规格实体
     */
    @BeanMapping(ignoreByDefault = true) // Ignore unmapped fields, including BaseEntity fields
    @Mapping(target = "name", source = "name")
    // product, values are ignored (handled in service)
    ProductSpecification specCommandToEntity(CreateProductSpecificationCommand command);

    // --- Helper Methods for Collections ---

    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<ProductImage> images) {
        if (images == null) {
            return Collections.emptyList();
        }
        return images.stream()
                .sorted(Comparator.comparingInt(ProductImage::getSortOrder))
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Named("mapSpecificationsToViewDtos")
    default List<SpecificationViewDto> mapSpecificationsToViewDtos(List<ProductSpecification> specifications) {
        if (specifications == null) {
            return Collections.emptyList();
        }
        return specifications.stream()
                .map(this::mapSpecificationToViewDto)
                .collect(Collectors.toList());
    }

    @Named("mapSkusToDtos")
    default List<ProductSkuDto> mapSkusToDtos(List<ProductSku> skus) {
        if (skus == null) {
            return Collections.emptyList();
        }
        return skus.stream()
                .map(this::skuEntityToDto)
                .collect(Collectors.toList());
    }
    
    // --- Helper Methods for Single Objects ---
    
    // Maps ProductSpecification Entity to SpecificationViewDto
    @Mapping(target = "values", source = "values", qualifiedByName = "mapSpecValuesToDtos")
    SpecificationViewDto mapSpecificationToViewDto(ProductSpecification specification);
    
    // Maps ProductSku Entity to ProductSkuDto
    @Mapping(target = "isEnabled", source = "enabled") // maps entity 'enabled' (boolean) -> dto 'isEnabled' (Boolean)
    @Mapping(target = "isVerified", ignore=true) // isVerified is not in ProductSku entity
    ProductSkuDto skuEntityToDto(ProductSku sku);

    // --- Helper Methods for Inner Collections ---

    // Maps List<SpecificationValue> to List<SpecValueDto>
    @Named("mapSpecValuesToDtos")
    default List<SpecValueDto> mapSpecValuesToDtos(List<SpecificationValue> values) {
        if (values == null) {
            return Collections.emptyList();
        }
        return values.stream()
                .map(specValue -> new SpecValueDto(specValue.getId(), specValue.getValue()))
                .collect(Collectors.toList());
    }
} 