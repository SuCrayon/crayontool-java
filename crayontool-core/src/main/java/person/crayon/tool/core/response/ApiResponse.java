package person.crayon.tool.core.response;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import person.crayon.tool.core.common.Copyable;
import person.crayon.tool.core.desensitization.DesensitizeField;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/5/21 21:56
 * 统一Api响应结果实体类
 */
@Data
@Accessors(chain = true)
public class ApiResponse implements Copyable<ApiResponse> {
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
    private Map<String, Object> data;

    public ApiResponse() {
        this.code = "";
        this.message = "";
        this.timestamp = System.currentTimeMillis();
        // data懒初始化
        // this.data = new HashMap<>();
    }

    public ApiResponse(ResponseStatus status) {
        this();
        this.setStatus(status);
    }

    /**
     * 如果有必要，初始化data
     */
    private void initDataIfNeed() {
        if (this.data == null) {
            synchronized (this) {
                if (this.data == null) {
                    this.data = new HashMap<>();
                }
            }
        }
    }

    /**
     * 扁平化添加元素
     * Object: { name: "Crayon", age: 18 }
     * data: { name: "Crayon", age: 18 }
     * @param value 值
     * @return ApiResponse
     */
    public ApiResponse assignPut(Object value) {
        this.initDataIfNeed();
        this.data.putAll(BeanUtil.beanToMap(value));
        return this;
    }

    /**
     * data中添加单个元素
     *
     * @param key   键
     * @param value 值
     * @return ApiResponse
     */
    public ApiResponse putItem(String key, Object value) {
        this.initDataIfNeed();
        this.data.put(key, value);
        return this;
    }

    /**
     * data中批量添加元素
     *
     * @param map 元素Map
     * @return ApiResponse
     */
    public ApiResponse putItems(Map<String, Object> map) {
        if (map == null) {
            return this;
        }
        this.initDataIfNeed();
        this.data.putAll(map);
        return this;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     * @return ApiResponse
     */
    public ApiResponse setStatus(ResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        return this;
    }

    /**
     * 避免JSON序列化时没有初始化返回null造成不必要的麻烦
     *
     * @return Map
     */
    public Map<String, Object> getData() {
        this.initDataIfNeed();
        return this.data;
    }


    @Override
    public ApiResponse copy() {
        return new ApiResponse()
                .setCode(this.code)
                .setMessage(this.message)
                .setData(this.data);
    }
}
