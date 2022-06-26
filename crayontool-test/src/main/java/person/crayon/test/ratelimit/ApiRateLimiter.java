package person.crayon.test.ratelimit;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import person.crayon.tool.core.common.domain.ratelimit.LimitRule;
import person.crayon.tool.core.ratelimit.limiter.FixedWindowLimiter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Crayon
 * @date 2022/6/26 16:00
 */
//@Component
//@Order(2)
public class ApiRateLimiter extends FixedWindowLimiter {
    @Override
    protected String getThrottleId(HttpServletRequest request) {
        return null;
    }

    @Override
    protected List<LimitRule> loadLimitRule() {
        return null;
    }
}
