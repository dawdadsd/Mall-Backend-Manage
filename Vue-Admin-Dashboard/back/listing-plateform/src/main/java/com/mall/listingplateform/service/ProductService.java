package com.mall.listingplateform.service;

import com.mall.listingplateform.dto.ProductDTO;
import com.mall.listingplateform.dto.ProductQueryParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductService {
    Page<ProductDTO> findAll(ProductQueryParams queryParams, Pageable pageable);
    ProductDTO findById(Long id);
    ProductDTO save(ProductDTO productDTO);
    void delete(Long id);
    Map<Long,String> getALlCategories();
    Map<String,String> getAllStatuses();
}
