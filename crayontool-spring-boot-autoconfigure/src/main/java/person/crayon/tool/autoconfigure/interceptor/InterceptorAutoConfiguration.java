package person.crayon.tool.autoconfigure.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import person.crayon.tool.core.log.interceptor.ApiLogInterceptor;

/**
 * @author Crayon
 * @date 2022/5/23 6:51
 * 拦截器自动配置类
 */
@Configuration
public class InterceptorAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiLogInterceptor()).addPathPatterns("/**");
    }
}
