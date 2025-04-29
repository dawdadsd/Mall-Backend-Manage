package com.mall.listingplateform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务逻辑异常
 * 用于表示业务逻辑处理中的错误
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    
    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 静态工厂方法：创建资源不存在异常
     * 
     * @param resourceType 资源类型
     * @param id 资源ID
     * @return 业务异常
     */
    public static BusinessException resourceNotFound(String resourceType, Object id) {
        return new BusinessException(
                HttpStatus.NOT_FOUND.value(), 
                String.format("%s不存在，ID: %s", resourceType, id));
    }

    /**
     * 静态工厂方法：创建资源已存在异常
     * 
     * @param resourceType 资源类型
     * @param field 字段名
     * @param value 字段值
     * @return 业务异常
     */
    public static BusinessException resourceAlreadyExists(String resourceType, String field, Object value) {
        return new BusinessException(
                HttpStatus.CONFLICT.value(), 
                String.format("%s已存在，%s: %s", resourceType, field, value));
    }

    /**
     * 静态工厂方法：创建操作不允许异常
     * 
     * @param message 错误信息
     * @return 业务异常
     */
    public static BusinessException operationNotAllowed(String message) {
        return new BusinessException(HttpStatus.FORBIDDEN.value(), message);
    }

    /**
     * 静态工厂方法：创建参数无效异常
     * 
     * @param message 错误信息
     * @return 业务异常
     */
    public static BusinessException invalidParameter(String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST.value(), message);
    }
} 