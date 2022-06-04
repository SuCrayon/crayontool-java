package person.crayon.tool.core.common.domain.ratelimit;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Crayon
 * @date 2022/6/3 21:56
 * 限流记录类
 */
@Data
@ToString
@Accessors(chain = true)
public class LimitRecord {
    /**
     * 上次请求的时间
     */
    private long lastRequestTime;
    /**
     * 请求数记录
     */
    private int count;

    /**
     * 清空计数
     * @return
     */
    public synchronized int clearCount() {
        this.count = 0;
        return this.count;
    }

    /**
     * 自增计数
     * @return
     */
    public synchronized int incrCount() {
        return ++this.count;
    }
}
