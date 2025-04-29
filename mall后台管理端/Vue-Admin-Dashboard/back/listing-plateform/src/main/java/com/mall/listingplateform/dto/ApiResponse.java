package com.mall.listingplateform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * API统一响应结构
 * 在2025年前后端交互准则中，推荐使用统一的响应结构
 *
 * @param <T> 返回数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * 请求是否成功
     */
    private boolean success;
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 响应时间戳
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * 跟踪ID，用于全链路追踪
     */
    private String traceId;
    
    /**
     * 创建成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message("操作成功")
                .data(data)
                .build();
    }
    
    /**
     * 创建失败响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
} 