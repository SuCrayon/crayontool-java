package person.crayon.tool.core.ratelimit.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreq;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreqJsonFileParser;
import person.crayon.tool.core.ratelimit.limiter.FixedWindowLimiter;

import java.util.Map;

/**
 * @author Crayon
 * @date 2022/6/2 10:59
 * 默认的限流拦截器
 */
@Slf4j
@ConditionalOnMissingBean(RateLimitInterceptor.class)
public class DefaultRateLimitInterceptor extends RateLimitInterceptor {
    /**
     * 限流频率json文件路径
     */
    @Value("${person.crayon.tool.rate-limit.limit-freq-json-path:limit-freq.json}")
    private String path;

    /**
     * 限流频率Map
     */
    private Map<String, LimitFreq> limitFreqMap;

    /**
     * 初始化限流频率Map
     */
    private void initLimitFreqMap() {
        limitFreqMap = LimitFreqJsonFileParser.parse(path);
    }

    @Override
    protected void initLimiter() {
        limiter = new FixedWindowLimiter();
    }

    @Override
    protected Map<String, LimitFreq> getLimitFreq() {
        return limitFreqMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        log.debug("{}: limit-freq-json-path: {}", this.getClass().getSimpleName(), path);
        log.debug("{}: init limit-freq-map...", this.getClass().getSimpleName());
        initLimitFreqMap();
    }
}
