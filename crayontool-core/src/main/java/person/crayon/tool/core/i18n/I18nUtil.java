package person.crayon.tool.core.i18n;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import person.crayon.tool.core.i18n.plural.PluralSyntaxParser;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Crayon
 * @date 2022/5/21 23:25
 * 国际化工具类
 */
public class I18nUtil {
    @Resource
    private MessageSource messageSource;

    @Resource(name = "pluralSyntaxParser")
    private PluralSyntaxParser syntaxParser;

    /**
     * 获取原始国际化消息，不渲染填充参数值
     * @param key
     * @return
     */
    public String getMessageWithoutRender(String key) {
        return messageSource.getMessage(key, ArrayUtil.newArray(0), key, LocaleContextHolder.getLocale());
    }

    /**
     * 获取原始国际化消息，不渲染填充参数值，指定locale
     * @param key
     * @param locale
     * @return
     */
    public String getMessageWithoutRender(String key, Locale locale) {
        return messageSource.getMessage(key, ArrayUtil.newArray(0), key, locale);
    }

    /**
     * 复数表达式解析的国际化消息获取
     * @param key
     * @param args
     * @return
     */
    public String getMessageWithPluralSyntax(String key, Object... args) {
        String s = getMessageWithoutRender(key);
        s = syntaxParser.parse(s, args);
        return MessageFormat.format(s, args);
    }

    /**
     * 复数表达式解析的国际化消息获取，指定locale
     * @param key
     * @param locale
     * @param args
     * @return
     */
    public String getMessageWithPluralSyntax(String key, Locale locale, Object... args) {
        String s = getMessageWithoutRender(key, locale);
        s = syntaxParser.parse(s, args);
        return MessageFormat.format(s, args);
    }

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
     * 获取指定locale的国际化消息，不抛出异常
     * @param key
     * @param locale
     * @param args
     * @return
     */
    public String tryGetMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, key, locale);
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

    /**
     * 获取指定locale的国际化消息
     * @param key
     * @param locale
     * @param args
     * @return
     */
    public String getMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }
}
