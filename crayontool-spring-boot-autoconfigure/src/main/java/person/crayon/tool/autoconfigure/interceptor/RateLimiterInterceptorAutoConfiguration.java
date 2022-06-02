package person.crayon.tool.autoconfigure.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import person.crayon.tool.core.common.constants.InterceptorOrder;
import person.crayon.tool.core.log.interceptor.ApiLogInterceptor;
import person.crayon.tool.core.ratelimiter.interceptor.RateLimiterInterceptor;

import javax.annotation.Resource;

/**
 * @author Crayon
 * @date 2022/5/23 6:51
 * 限流拦截器自动配置类
 */
@Configuration
@ConditionalOnProperty(prefix = "person.crayon.tool.rate-limiter.interceptor", value = "enable", havingValue = "true")
public class RateLimiterInterceptorAutoConfiguration implements WebMvcConfigurer {
    @Value("${person.crayon.tool.rate-limiter.interceptor.add-path-patterns:/**}")
    private String[] addPathPatterns;

    @Value("${person.crayon.tool.rate-limiter.interceptor.exclude-path-patterns:}")
    private String[] excludePathPatterns;

    @Value("${person.crayon.tool.rate-limiter.interceptor.order:#{T(person.crayon.tool.core.common.constants.InterceptorOrder).RATE_LIMITER_ORDER}}")
    private int order;

    @Resource
    private RateLimiterInterceptor rateLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor)
                .addPathPatterns(addPathPatterns)
                .excludePathPatterns(excludePathPatterns)
                .order(order);
    }
}
