package com.mall.productplatform.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.common.MediaTypes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 采用RFC 7807规范的Problem Details格式返回错误信息
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("资源未找到异常", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        log.error("业务逻辑异常", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("参数验证异常", ex);
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Problem problem = Problem.builder()
                .withType(URI.create("https://api.mall.com/errors/validation-error"))
                .withTitle("参数验证错误")
                .withStatus(Status.BAD_REQUEST)
                .withDetail("提交的数据未通过验证")
                .with("errors", errors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaTypes.PROBLEM)
                .body(problem);
    }
    
    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("约束违反异常", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                "约束违反：" + ex.getMessage());
        
        Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            violations.put(path, message);
        });
        
        problemDetail.setProperty("violations", violations);
        return problemDetail;
    }
    
    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("方法参数类型不匹配异常", ex);
        
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                String.format("参数 '%s' 的值 '%s' 类型不匹配，应为 %s", 
                        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    }
    
    /**
     * 处理乐观锁异常
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        log.error("乐观锁异常", ex);
        
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                "数据已被另一个用户修改，请刷新后重试");
    }
    
    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        log.error("访问拒绝异常", ex);
        
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, 
                "您没有权限访问此资源");
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("未处理的异常", ex);
        
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "服务器内部错误，请联系管理员");
    }
} 