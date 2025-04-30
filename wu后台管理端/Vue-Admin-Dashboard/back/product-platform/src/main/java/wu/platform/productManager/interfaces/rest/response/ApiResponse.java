package wu.platform.productManager.interfaces.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API响应包装类
 * 用于统一返回格式
 *
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 创建成功响应
     *
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, "00000", "操作成功", null);
    }

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "00000", "操作成功", data);
    }

    /**
     * 创建成功响应
     *
     * @param message 成功消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, "00000", message, data);
    }

    /**
     * 创建失败响应
     *
     * @param code    错误代码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    /**
     * 创建失败响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, "B0001", message, null);
    }
} 