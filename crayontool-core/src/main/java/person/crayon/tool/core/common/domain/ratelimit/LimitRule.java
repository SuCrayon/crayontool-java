package person.crayon.tool.core.common.domain.ratelimit;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/26 12:48
 * 限流规则
 */
@Data
@Accessors(chain = true)
@ToString
public class LimitRule {
    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 限流值
     */
    private int amount;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 设置默认空串
     * @param description 描述信息
     */
    public void setDescription(String description) {
        this.description = Optional.ofNullable(description).orElse(StrUtil.EMPTY);
    }

    /**
     * 获取毫秒级时间跨度
     *
     * @return 毫秒数
     */
    public long getMilliSpan() {
        return unit.toMillis(1);
    }

    /**
     * 获取秒级时间跨度
     *
     * @return 秒数
     */
    public long getSecondSpan() {
        return unit.toSeconds(1);
    }
}
