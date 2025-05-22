package com.mall.listingplateform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 * 
 * 封装业务逻辑相关的异常，提供错误码、错误信息和HTTP状态码
 * 便于全局异常处理器进行特殊处理
 * 
 * @since 2025
 * @version 2.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus status;

    /**
     * 创建业务异常
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public BusinessException(String errorCode, String message) {
        this(errorCode, message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 创建业务异常
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     * @param status HTTP状态码
     */
    public BusinessException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
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
                "NOT_FOUND", 
                String.format("%s不存在，ID: %s", resourceType, id),
                HttpStatus.NOT_FOUND);
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
                "ALREADY_EXISTS", 
                String.format("%s已存在，%s: %s", resourceType, field, value),
                HttpStatus.CONFLICT);
    }

    /**
     * 静态工厂方法：创建操作不允许异常
     * 
     * @param message 错误信息
     * @return 业务异常
     */
    public static BusinessException operationNotAllowed(String message) {
        return new BusinessException("OPERATION_NOT_ALLOWED", message, HttpStatus.FORBIDDEN);
    }

    /**
     * 静态工厂方法：创建参数无效异常
     * 
     * @param message 错误信息
     * @return 业务异常
     */
    public static BusinessException invalidParameter(String message) {
        return new BusinessException("INVALID_PARAMETER", message, HttpStatus.BAD_REQUEST);
    }
} 