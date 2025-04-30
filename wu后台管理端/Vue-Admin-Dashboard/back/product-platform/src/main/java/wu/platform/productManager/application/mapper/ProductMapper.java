package wu.platform.productManager.application.mapper;

import wu.platform.productManager.application.dto.command.CreateProductCommand;
import wu.platform.productManager.application.dto.command.CreateProductSkuCommand;
import wu.platform.productManager.application.dto.query.ProductDto;
import wu.platform.productManager.application.dto.query.ProductSkuDto;
import wu.platform.productManager.application.dto.query.SpecificationViewDto;
import com.mall.productplatform.domain.entity.*;
import org.mapstruct.*;
import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.entity.ProductImage;
import wu.platform.productManager.domain.entity.ProductSku;
import wu.platform.productManager.domain.entity.ProductSpecification;

import java.util.*;
import java.util.stream.Collectors;

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
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "mapSpecifications")
    @Mapping(target = "skus", source = "skus", qualifiedByName = "mapSkus")
    ProductDto toDto(Product product);

    /**
     * 将创建商品命令转换为商品实体
     * 
     * @param command 创建商品命令
     * @return 商品实体
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "condition", source = "condition")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "sales", constant = "0")
    @Mapping(target = "inventory", constant = "0")
    Product toEntity(CreateProductCommand command);

    /**
     * 将SKU命令转换为SKU实体
     * 
     * @param command SKU命令
     * @return SKU实体
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "inventory", source = "inventory")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "sales", constant = "0")
    @Mapping(target = "isEnabled", constant = "true")
    ProductSku toEntity(CreateProductSkuCommand command);

    /**
     * 将图片集合映射为URL列表
     * 
     * @param images 商品图片集合
     * @return 图片URL列表
     */
    @Named("mapImages")
    default List<String> mapImages(Set<ProductImage> images) {
        if (images == null) {
            return Collections.emptyList();
        }
        
        return images.stream()
                .sorted(Comparator.comparing(ProductImage::getSortOrder))
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());
    }

    /**
     * 将规格集合映射为规格视图DTO列表
     * 
     * @param specifications 规格集合
     * @return 规格视图DTO列表
     */
    @Named("mapSpecifications")
    default List<SpecificationViewDto> mapSpecifications(Set<ProductSpecification> specifications) {
        if (specifications == null) {
            return Collections.emptyList();
        }
        
        Map<String, List<SpecificationViewDto.SpecValueDto>> specMap = new HashMap<>();
        
        for (ProductSpecification spec : specifications) {
            String name = spec.getName();
            if (!specMap.containsKey(name)) {
                specMap.put(name, new ArrayList<>());
            }
            
            SpecificationViewDto.SpecValueDto valueDto = new SpecificationViewDto.SpecValueDto(
                    spec.getId(),
                    spec.getValue());
            
            specMap.get(name).add(valueDto);
        }
        
        return specMap.entrySet().stream()
                .map(entry -> SpecificationViewDto.builder()
                        .name(entry.getKey())
                        .values(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 将SKU集合映射为SKU DTO列表
     * 
     * @param skus SKU集合
     * @return SKU DTO列表
     */
    @Named("mapSkus")
    default List<ProductSkuDto> mapSkus(Set<ProductSku> skus) {
        if (skus == null) {
            return Collections.emptyList();
        }
        
        return skus.stream()
                .map(sku -> {
                    Map<String, String> specMap = new HashMap<>();
                    // 此处应该有更复杂的逻辑来处理SKU与规格值的关联
                    
                    return ProductSkuDto.builder()
                            .skuCode(sku.getSkuCode())
                            .price(sku.getPrice())
                            .inventory(sku.getInventory())
                            .sales(sku.getSales())
                            .imageUrl(sku.getImageUrl())
                            .specifications(specMap)
                            .enabled(sku.getIsEnabled())
                            .build();
                })
                .collect(Collectors.toList());
    }
} 