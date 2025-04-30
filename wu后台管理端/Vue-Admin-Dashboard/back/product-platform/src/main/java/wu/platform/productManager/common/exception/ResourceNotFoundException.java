package wu.platform.productManager.common.exception;

/**
 * 资源未找到异常
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceType, Long id) {
        super(String.format("%s 不存在，ID：%d", resourceType, id));
    }
    
    public ResourceNotFoundException(String resourceType, String code) {
        super(String.format("%s 不存在，代码：%s", resourceType, code));
    }
} 