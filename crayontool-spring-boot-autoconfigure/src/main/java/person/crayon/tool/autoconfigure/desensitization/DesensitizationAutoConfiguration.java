package person.crayon.tool.autoconfigure.desensitization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import person.crayon.tool.core.desensitization.aspect.DesensitizeAspect;
import person.crayon.tool.core.desensitization.desensitizer.Desensitizer;
import person.crayon.tool.core.desensitization.desensitizer.impl.IteratorDesensitizer;
import person.crayon.tool.core.desensitization.desensitizer.impl.MapDesensitizer;
import person.crayon.tool.core.desensitization.desensitizer.impl.StringDesensitizer;

/**
 * @author Crayon
 * @date 2022/10/2 14:41
 */
@Configuration
@ConditionalOnProperty(prefix = "person.crayon.tool.desensitization", value = "enable", havingValue = "true")
public class DesensitizationAutoConfiguration {

    @Bean
    public DesensitizeAspect desensitizeAspect() {
        return new DesensitizeAspect();
    }

    @Bean
    public Desensitizer stringDesensitizer() {
        return new StringDesensitizer();
    }

    @Bean
    public Desensitizer iteratorDesensitizer() {
        return new IteratorDesensitizer();
    }

    @Bean
    public Desensitizer mapDesensitizer() {
        return new MapDesensitizer();
    }
}
