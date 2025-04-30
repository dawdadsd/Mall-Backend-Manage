package com.mall.productplatform.domain.vo;

import lombok.Getter;

/**
 * 商品品相评级枚举
 * 使用Java现代化设计，带有丰富的业务属性
 */
@Getter
public enum ProductCondition {
    NEW("全新", "未使用，包装完好", 10),
    EXCELLENT("9成新", "轻微使用痕迹，功能完好", 9),
    GOOD("8成新", "正常使用痕迹，轻微划痕/磨损", 8),
    FAIR("7成新", "明显使用痕迹，功能可能有小瑕疵", 7),
    POOR("6成新及以下", "外观磨损严重或功能有缺陷", 6);
    
    private final String label;
    private final String description;
    private final int score;
    
    ProductCondition(String label, String description, int score) {
        this.label = label;
        this.description = description;
        this.score = score;
    }
    
    /**
     * 判断是否达到 "优质" 标准（8成新及以上）
     */
    public boolean isPremiumQuality() {
        return this.score >= 8;
    }
    
    /**
     * 根据评分获取对应的品相等级
     */
    public static ProductCondition fromScore(int score) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }
        
        if (score == 10) return NEW;
        if (score == 9) return EXCELLENT;
        if (score == 8) return GOOD;
        if (score == 7) return FAIR;
        return POOR;
    }
} 