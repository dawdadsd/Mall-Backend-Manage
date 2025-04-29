package com.mall.listingplateform.service;

import com.mall.listingplateform.dto.ProductDTO;

import java.util.List;
import java.util.Map;

/**
 * 商品数据清洗服务接口 - 简化版
 * 
 * 提供商品数据质量检查、数据修复和清洗规则管理功能
 * 当前为框架版本，后续可扩展完整功能
 * 
 * @since 2025
 * @version 1.0
 */
public interface ProductDataCleanService {
    
    /**
     * 检查单个商品数据质量
     * 
     * @param productId 商品ID
     * @return 数据质量检查结果
     */
    Map<String, Object> checkProductQuality(Long productId);
    
    /**
     * 批量检查商品数据质量
     * 
     * @param productIds 商品ID列表
     * @return 各商品的数据质量检查结果
     */
    Map<Long, Map<String, Object>> batchCheckProductsQuality(List<Long> productIds);
    
    /**
     * 修复商品数据
     * 
     * @param productId 商品ID
     * @return 修复后的商品信息
     */
    ProductDTO repairProductData(Long productId);
    
    /**
     * 获取数据质量报告
     * 
     * @return 数据质量统计报告
     */
    Map<String, Object> getDataQualityReport();
    
    /**
     * 应用智能清洗规则
     * 
     * @param productId 商品ID
     * @return 清洗后的商品信息
     */
    ProductDTO applyCleaningRules(Long productId);
    
    /**
     * 获取所有可用的清洗规则
     * 
     * @return 清洗规则列表
     */
    List<Map<String, Object>> getAvailableCleaningRules();
} 