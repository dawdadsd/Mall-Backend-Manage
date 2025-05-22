package com.mall.listingplateform.dto;

import lombok.Data;

@Data
public class ProductQueryParams {
    private String name;
    private String categoryId;
    private String status;
    private Integer minFansCount;
    private Integer maxFansCount;
    private Long merchantId;
    private Double minPrice;
    private Double maxPrice;
}
