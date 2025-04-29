package com.mall.listingplateform.exception;

import com.mall.listingplateform.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 全局异常处理器
 * 统一处理API异常，返回标准响应格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String traceId = generateTraceId();
        logger.error("资源不存在异常 [{}]: {}", traceId, ex.getMessage(), ex);
        
        return ApiResponse.builder()
                .success(false)
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .traceId(traceId)
                .build();
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String traceId = generateTraceId();
        logger.error("参数校验异常 [{}]: {}", traceId, ex.getMessage(), ex);
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ApiResponse.<Map<String, String>>builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("输入参数验证失败")
                .data(errors)
                .traceId(traceId)
                .build();
    }
    
    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String traceId = generateTraceId();
        logger.error("约束违反异常 [{}]: {}", traceId, ex.getMessage(), ex);
        
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        
        return ApiResponse.<Map<String, String>>builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("输入参数验证失败")
                .data(errors)
                .traceId(traceId)
                .build();
    }

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBusinessException(BusinessException ex) {
        String traceId = generateTraceId();
        logger.error("业务逻辑异常 [{}]: {}", traceId, ex.getMessage(), ex);
        
        return ApiResponse.builder()
                .success(false)
                .code(ex.getCode())
                .message(ex.getMessage())
                .traceId(traceId)
                .build();
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGenericException(Exception ex) {
        String traceId = generateTraceId();
        logger.error("未预期的异常 [{}]: {}", traceId, ex.getMessage(), ex);
        
        return ApiResponse.builder()
                .success(false)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("服务器内部错误，请联系管理员")
                .traceId(traceId)
                .build();
    }
    
    /**
     * 生成唯一的跟踪ID
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
} 