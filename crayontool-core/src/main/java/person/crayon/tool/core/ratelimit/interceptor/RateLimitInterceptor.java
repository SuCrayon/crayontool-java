package person.crayon.tool.core.ratelimit.interceptor;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreq;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreqJsonFileParser;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;
import person.crayon.tool.core.common.utils.HttpUtil;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.ratelimit.limiter.Limiter;
import person.crayon.tool.core.response.ApiResponse;
import person.crayon.tool.core.response.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/2 10:59
 * 限流拦截器
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "person.crayon.tool.rate-limit", value = "enable", havingValue = "true")
public abstract class RateLimitInterceptor implements HandlerInterceptor, InitializingBean {
    /**
     * 响应码
     */
    @Value("${person.crayon.tool.rate-limit.code:429}")
    private String code;

    /**
     * 响应消息
     */
    @Value("${person.crayon.tool.rate-limit.message:Too Many Requests}")
    private String message;

    /**
     * Status原型
     */
    private ResponseStatus responseStatus;

    /**
     * 限流器
     */
    protected Limiter limiter;

    /**
     * 初始化限流器
     */
    protected abstract void initLimiter();

    /**
     * 获取限流频率Map
     * @return Map
     */
    protected abstract Map<String, LimitFreq> getLimitFreq();

    /**
     * 获取限流标识
     * 默认实现则是取ip+uri作为标识
     * @param uri uri
     * @return 限流标识
     */
    protected String getThrottleId(String uri) {
        return StrUtil.format("{}-{}", uri, HttpUtil.getRealIp());
    }

    /**
     * 设置响应头
     * @param response 响应对象
     * @param result 限流结果
     */
    protected void setHeaders(HttpServletResponse response, LimitResult result) {
        /*
        response.setHeader("X-RateLimit-Limit", "每小时允许的最大请求数");
        response.setHeader("X-RateLimit-Remaining", "当前限流中剩余的请求次数");
        response.setHeader("X-RateLimit-Reset", "以UTC秒为单位，当前限流的重置时间");
         */

        // 剩余请求次数
        HttpUtil.setHeaderValue(response,"X-RateLimit-Remaining", result.getRemainCount());
        // 重置时间
        HttpUtil.setHeaderValue(response, "X-RateLimit-ResetTime", TimeUnit.MILLISECONDS.toSeconds(result.getRemainResetTime()));
    }

    /**
     * 限流
     * @param uri uri
     * @return 限流结果，是否放行
     */
    protected LimitResult rateLimit(String uri, HttpServletResponse response) {
        Map<String, LimitFreq> limitFreqMap = getLimitFreq();
        if (!limitFreqMap.containsKey(uri)) {
            return LimitResult.allow();
        }
        // 限流器逻辑
        LimitResult result = limiter.check(getThrottleId(uri), limitFreqMap.get(uri));
        setHeaders(response, result);
        return result;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("===> rate-limit-interceptor");
        String uri = request.getRequestURI();
        LimitResult result = rateLimit(uri, response);
        if (result.isAllow()) {
            return true;
        }
        throw new CustomException(responseStatus.copy());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化响应状态的原型类
        responseStatus = new ResponseStatus(code, message);
        log.debug("{}: init limier...", this.getClass().getSimpleName());
        initLimiter();
    }
}
