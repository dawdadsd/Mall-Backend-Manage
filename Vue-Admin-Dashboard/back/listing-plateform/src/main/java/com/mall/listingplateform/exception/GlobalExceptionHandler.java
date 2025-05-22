package com.mall.listingplateform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 
 * 统一处理系统中可能出现的各类异常，提供标准化的错误响应格式
 * 支持细粒度的异常分类和友好的错误信息
 * 
 * @since 2025
 * @version 2.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * 
     * @param ex 业务异常
     * @return 错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        log.warn("业务异常 [{}]: {}", ex.getErrorCode(), ex.getMessage());
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", ex.getStatus().value());
        body.put("error", ex.getStatus().getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("code", ex.getErrorCode());
        
        return new ResponseEntity<>(body, ex.getStatus());
    }

    /**
     * 处理资源不存在异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NoSuchElementException ex) {
        log.error("资源不存在: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "资源不存在", ex.getMessage());
    }

    /**
     * 处理参数校验失败异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.error("参数校验失败: {}", errorMessages);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "参数校验失败", errorMessages);
    }
    
    /**
     * 处理绑定异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBindExceptions(BindException ex) {
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.error("参数绑定失败: {}", errorMessages);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "参数绑定失败", errorMessages);
    }

    /**
     * 处理参数类型不匹配异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "参数'%s'的值'%s'类型不匹配，需要类型: %s", 
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() == null ? "未知" : ex.getRequiredType().getSimpleName());
        
        log.error("参数类型不匹配: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "参数类型不匹配", message);
    }

    /**
     * 处理非法参数异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("非法参数: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "非法参数", ex.getMessage());
    }

    /**
     * 处理通用异常
     * 
     * @param ex 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("系统异常", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "系统异常", 
                "系统处理请求时发生错误，请稍后重试");
    }

    /**
     * 构建标准化错误响应
     * 
     * @param status HTTP状态码
     * @param error 错误类型
     * @param message 错误详细信息
     * @return 封装的错误响应
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        
        return new ResponseEntity<>(body, status);
    }
} 