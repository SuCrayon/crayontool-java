package person.crayon.tool.autoconfigure.exception.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import person.crayon.tool.core.exception.handler.ConstraintExceptionHandler;
import person.crayon.tool.core.exception.handler.CustomExceptionHandler;

/**
 * @author Crayon
 * @date 2022/5/23 0:15
 * 异常处理器自动配置类
 */
@Configuration
public class HandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CustomExceptionHandler.class)
    @ConditionalOnProperty(prefix = "person.crayon.tool.exception.handler.custom", value = "enable", havingValue = "true")
    public CustomExceptionHandler customExceptionHandler() {
        return new CustomExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(ConstraintExceptionHandler.class)
    @ConditionalOnProperty(prefix = "person.crayon.tool.exception.handler.constraint", value = "enable", havingValue = "true")
    public ConstraintExceptionHandler constraintExceptionHandler() {
        return new ConstraintExceptionHandler();
    }
}
