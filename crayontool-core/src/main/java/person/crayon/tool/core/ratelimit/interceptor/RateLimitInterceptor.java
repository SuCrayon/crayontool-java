package person.crayon.tool.core.ratelimit.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.ratelimit.limiter.Limiter;
import person.crayon.tool.core.response.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Crayon
 * @date 2022/6/2 10:59
 * 限流拦截器
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "person.crayon.tool.rate-limit", value = "enable", havingValue = "true")
public class RateLimitInterceptor implements HandlerInterceptor, InitializingBean {
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
    @Resource
    protected List<Limiter> limiters;

    protected void init() {
        // 初始化响应状态的原型类
        responseStatus = new ResponseStatus(code, message);
    }

    @Override
    public final void afterPropertiesSet() {
        init();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("===> rate-limit-interceptor");
        for (Limiter limiter : limiters) {
            if (limiter.check(request).isForbid()) {
                throw new CustomException(responseStatus.copy());
            }
        }
        return true;
    }

}
