package person.crayon.tool.core.ratelimiter.interceptor;

import cn.hutool.core.date.DateUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import person.crayon.tool.core.common.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Crayon
 * @date 2022/6/2 10:59
 * 限流拦截器
 */
@Slf4j
public abstract class RateLimiterInterceptor implements HandlerInterceptor {

    /**
     * 获取限流规则
     * @return 限流规则Map集合
     */
    protected abstract Map<String, String> getLimitRules();

    /**
     * 传入uri获取限流标识
     * 每个用户对于一个接口的限流标识
     * @param uri uri
     * @return 限流标识
     */
    protected abstract String getThrottleId(String uri);

    /**
     * 根据限流标识获取剩余的流量并更新
     * @param throttleId 限流标识
     * @return 剩余流量
     */
    protected abstract int getThrottleLeave(String throttleId);



    /**
     * 校验
     * @param rule 规则
     * @return 限流结果，是否放行
     */
    private boolean check(String uri, String rule) {
        // 获取限流标识
        String throttleId = getThrottleId(uri);
        // 获取剩余流量并更新
        int record = getThrottleLeave(throttleId);
        // 流量是否有剩余
        return record > 0;
    }

    /**
     * 限流
     * @param uri uri
     * @return 限流结果，是否放行
     */
    public boolean rateLimit(String uri) {
        Map<String, String> limitRules = Optional.ofNullable(getLimitRules()).orElse(new HashMap<>(0));
        if (!limitRules.containsKey(uri)) {
            // 不在规则内就默认放行
            log.debug("uri: {} not in rule-map", uri);
            return true;
        }

        return check(uri, limitRules.get(uri));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("===> rate-limiter-interceptor");
        String uri = request.getRequestURI();
        return rateLimit(uri);
    }
}
