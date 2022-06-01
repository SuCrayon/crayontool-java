package person.crayon.tool.autoconfigure.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import person.crayon.tool.core.captcha.check.filter.CheckCaptchaFilter;

@Configuration
@ConditionalOnProperty(prefix = "person.crayon.tool.captcha.check", value = "enable", havingValue = "true")
public class CheckCaptchaFilterAutoConfiguration {

    @Value("${person.crayon.tool.captcha.check.filter.url-patterns:/*}")
    private String[] urlPatterns;

    @Bean
    public FilterRegistrationBean companyUrlFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new CheckCaptchaFilter());
        //拦截规则
        registration.addUrlPatterns(urlPatterns);
        //过滤器名称
        registration.setName("CheckCaptchaFilter");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }
}
