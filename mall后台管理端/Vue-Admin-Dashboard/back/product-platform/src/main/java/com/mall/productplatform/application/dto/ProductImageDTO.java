package com.mall.productplatform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品图片DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO implements Serializable {
    
    private Long id;
    private String url;
    private Integer sort;
    private Boolean isMain;
    private String altText;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
} 