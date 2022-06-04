package person.crayon.tool.core.common.domain.ratelimit;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/3 20:42
 * 限流频率
 */
@Data
@Accessors(chain = true)
@ToString
public class LimitFreq {
    /**
     * 值
     */
    private int amount;
    /**
     * 时间单位
     */
    private TimeUnit unit;

    public LimitFreq() {
    }

    public LimitFreq(int amount, TimeUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * 获取毫秒级时间跨度
     * @return 毫秒数
     */
    public long getMilliSpan() {
        return unit.toMillis(1);
    }

    /**
     * 获取秒级时间跨度
     * @return 秒数
     */
    public long getSecondSpan() {
        return unit.toSeconds(1);
    }
}
