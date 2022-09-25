package person.crayon.test.ratelimit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import person.crayon.tool.core.common.domain.ratelimit.LimitRule;
import person.crayon.tool.core.common.utils.RateLimitUtil;
import person.crayon.tool.core.ratelimit.limiter.FixedWindowLimiter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Crayon
 * @date 2022/6/26 15:52
 */
@Component
@Order(999)
public class TenantRateLimiter extends FixedWindowLimiter {
    @Override
    protected String getThrottleId(HttpServletRequest request) {
        return RateLimitUtil.getThrottleIdWithIp(RateLimitUtil.getRequestPathWithMethod(request));
    }

    @Override
    protected List<LimitRule> loadLimitRule() {
        return RateLimitUtil.loadLimitRuleFile("limit-rule.json");
    }

    @Override
    protected boolean isCacheAutoClean() {
        return true;
    }
}
