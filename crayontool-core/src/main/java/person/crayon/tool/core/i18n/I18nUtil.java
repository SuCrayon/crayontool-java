package person.crayon.tool.core.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;

/**
 * @author Crayon
 * @date 2022/5/21 23:25
 * 国际化工具类
 */
public class I18nUtil {
    @Resource
    private MessageSource messageSource;

    /**
     * 通过key取国际化消息，本方法在对应key不存在时将key作为消息返回而不是抛出异常
     * @param key i18n-key
     * @param args 参数列表
     * @return 国际化消息 || key
     */
    public String tryGetMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }

    /**
     * 通过key取国际化消息，本方法在对应key不存在时抛出异常
     * @param key i18n-key
     * @param args 参数列表
     * @return 国际化消息
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
