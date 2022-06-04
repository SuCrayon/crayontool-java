package person.crayon.tool.core.ratelimit.limiter;

import person.crayon.tool.core.common.domain.ratelimit.LimitFreq;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;

/**
 * @author Crayon
 * @date 2022/6/3 21:16
 */
public interface Limiter {
    /**
     *  是否放行
     * @param throttleId 限流标识
     * @param limitFreq 限流频率
     * @return 放行结果
     */
    LimitResult check(String throttleId, LimitFreq limitFreq);
}
