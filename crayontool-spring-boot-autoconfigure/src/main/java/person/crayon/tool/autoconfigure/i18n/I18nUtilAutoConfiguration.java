package person.crayon.tool.autoconfigure.i18n;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import person.crayon.tool.core.i18n.I18nUtil;

/**
 * @author Crayon
 * @date 2022/5/23 0:07
 */
@Configuration
//@ConditionalOnMissingBean(I18nUtil.class)
public class I18nUtilAutoConfiguration {
    @Bean
    public I18nUtil i18nUtil() {
        return new I18nUtil();
    }
}
