package person.crayon.tool.core.ratelimiter.interceptor;

import cn.hutool.core.map.MapUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/6/2 10:59
 * 默认的限流拦截器
 */
@Component
@ConditionalOnMissingBean(RateLimiterInterceptor.class)
public class DefaultRateLimiterInterceptor extends RateLimiterInterceptor {

    @Override
    protected Map<String, String> getLimitRules() {
        Map<String, String> map = MapUtil.newHashMap();
        map.put("/crayontool-test/hello", "1/s");
        return map;
    }

    @Override
    protected String getThrottleId(String uri) {
        return null;
    }

    @Override
    protected int getThrottleLeave(String throttleId) {
        return 0;
    }

}
