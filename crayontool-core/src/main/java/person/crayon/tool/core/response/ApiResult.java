package person.crayon.tool.core.response;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import person.crayon.tool.core.common.Copyable;
import person.crayon.tool.core.desensitization.DesensitizeField;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Crayon
 * @date 2022/5/21 21:56
 * 统一Api响应结果实体类
 */
@Data
@Accessors(chain = true)
public class ApiResult<T> implements Copyable<ApiResult<T>> {
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 返回数据
     */
    private T data;

    public ApiResult() {
        this.code = "";
        this.message = "";
        this.timestamp = System.currentTimeMillis();
        // data懒初始化
        // this.data = new HashMap<>();
    }

    public ApiResult(ResponseStatus status) {
        this();
        this.setStatus(status);
    }

    /**
     * 设置状态
     *
     * @param status 状态
     * @return ApiResult
     */
    public ApiResult<T> setStatus(ResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        return this;
    }

    /**
     * 避免JSON序列化时没有初始化返回null造成不必要的麻烦
     *
     * @return Map
     */
    public Object getData() {
        return Optional.of((Object) this.data).orElseGet(Object::new);
    }

    /**
     * 获取原始data
     * @return T
     */
    @JsonIgnore
    public T getOriginData() {
        return this.data;
    }


    @Override
    public ApiResult<T> copy() {
        return new ApiResult<T>()
                .setCode(this.code)
                .setMessage(this.message)
                .setData(this.data);
    }
}
