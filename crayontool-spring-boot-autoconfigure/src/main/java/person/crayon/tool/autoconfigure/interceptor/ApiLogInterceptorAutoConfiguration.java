package person.crayon.tool.autoconfigure.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import person.crayon.tool.core.common.constants.InterceptorOrder;
import person.crayon.tool.core.log.interceptor.ApiLogInterceptor;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Crayon
 * @date 2022/5/23 6:51
 * 接口日志拦截器自动配置类
 */
@Configuration
@ConditionalOnProperty(prefix = "person.crayon.tool.log", value = "enable", havingValue = "true", matchIfMissing = true)
public class ApiLogInterceptorAutoConfiguration implements WebMvcConfigurer {

    @Value("${person.crayon.tool.log.interceptor.add-path-patterns:/**}")
    private String[] addPathPatterns;

    @Value("${person.crayon.tool.log.interceptor.exclude-path-patterns:}")
    private String[] excludePathPatterns;

    @Value("${person.crayon.tool.log.interceptor.order:#{T(person.crayon.tool.core.common.constants.InterceptorOrder).API_LOG_ORDER}}")
    private int order;

    @Resource
    private ApiLogInterceptor apiLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLogInterceptor)
                .addPathPatterns(addPathPatterns)
                .excludePathPatterns(excludePathPatterns)
                .order(order);
    }
}
