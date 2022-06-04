package person.crayon.tool.core.common.domain.ratelimit;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/4 15:10
 * 限流结果
 */
@Data
@Accessors(chain = true)
public class LimitResult {
    /**
     * 是否允许
     */
    private boolean allow;
    /**
     * 剩余请求次数
     */
    private int remainCount;
    /**
     * 剩余重置时间
     */
    private long remainResetTime;

    public static LimitResult allow() {
        return new LimitResult().setAllow(true);
    }

    public static LimitResult forbid() {
        return new LimitResult().setAllow(false);
    }
}
