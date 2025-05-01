package wu.platform.productManager.domain.vo;

import lombok.Getter;

/**
 * 商家信用等级枚举
 * 用于表示商家的信用等级状态
 */
@Getter
public enum MerchantCreditLevel {
    
    /**
     * A级信用 - 优秀
     * 信用分数: 90-100
     */
    A("优秀", 90, 100),
    
    /**
     * B级信用 - 良好
     * 信用分数: 80-89
     */
    B("良好", 80, 89),
    
    /**
     * C级信用 - 一般
     * 信用分数: 60-79
     */
    C("一般", 60, 79),
    
    /**
     * D级信用 - 较差
     * 信用分数: 40-59
     */
    D("较差", 40, 59),
    
    /**
     * E级信用 - 差
     * 信用分数: 0-39
     */
    E("差", 0, 39);
    
    /**
     * 信用等级描述
     */
    private final String description;
    
    /**
     * 分数下限
     */
    private final int minScore;
    
    /**
     * 分数上限
     */
    private final int maxScore;
    
    /**
     * 构造函数
     *
     * @param description 信用等级描述
     * @param minScore 分数下限
     * @param maxScore 分数上限
     */
    MerchantCreditLevel(String description, int minScore, int maxScore) {
        this.description = description;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }
    
    /**
     * 根据信用分数获取信用等级
     *
     * @param score 信用分数
     * @return 对应的信用等级
     */
    public static MerchantCreditLevel fromScore(int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("信用分数必须在0-100范围内");
        }
        
        for (MerchantCreditLevel level : values()) {
            if (score >= level.getMinScore() && score <= level.getMaxScore()) {
                return level;
            }
        }
        
        // 默认返回C级
        return C;
    }
    
    /**
     * 检查分数是否在该等级范围内
     *
     * @param score 要检查的分数
     * @return 是否符合当前等级
     */
    public boolean isInRange(int score) {
        return score >= minScore && score <= maxScore;
    }
} 