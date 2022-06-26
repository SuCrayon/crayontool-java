package person.crayon.tool.core.ratelimit.limiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;
import person.crayon.tool.core.common.domain.ratelimit.LimitRule;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * @author Crayon
 * @date 2022/6/3 21:16
 */
@Slf4j
public abstract class Limiter {
    protected List<LimitRule> limitRules;

    public Limiter() {
        this.limitRules = loadLimitRule();
    }

    /**
     * 获取限流标识
     *
     * @param request request
     * @return 限流标识
     */
    protected abstract String getThrottleId(HttpServletRequest request);

    /**
     * 加载限流规则
     *
     * @return 规则列表
     */
    protected abstract List<LimitRule> loadLimitRule();

    /**
     * 限流规则校验
     *
     * @param throttleId 限流标识
     * @param limitRule  限流规则
     * @return 限流结果
     */
    protected abstract LimitResult checkRule(String throttleId, LimitRule limitRule);

    /**
     * 是否放行
     *
     * @param request 限流标识
     * @return 放行结果
     */
    public LimitResult check(HttpServletRequest request) {
        log.debug("===> {} checking", this.getClass().getSimpleName());
        for (LimitRule limitRule : limitRules) {
            if (limitRule.getMethod().matches(request.getMethod().toUpperCase(Locale.ENGLISH))
                    && PatternMatchUtils.simpleMatch(limitRule.getUri(), request.getRequestURI())
            ) {
                // 请求方法和请求uri都对应
                return checkRule(getThrottleId(request), limitRule);
            }
        }
        return LimitResult.allow();
    }
}
