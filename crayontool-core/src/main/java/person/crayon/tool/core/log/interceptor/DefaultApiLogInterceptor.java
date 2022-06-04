package person.crayon.tool.core.log.interceptor;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import person.crayon.tool.core.common.domain.ApiLog;

import java.text.MessageFormat;

/**
 * @author Crayon
 * @date 2022/6/2 11:49
 * 默认的接口日志打印拦截器
 * 统一封装了请求时间、请求method、请求uri、接口响应时间、返回结果等
 * eg. 2022/5/21 23:22:00 【GET】 /api/get 100ms ===> { ... }
 */
@Slf4j
@ConditionalOnMissingBean(ApiLogInterceptor.class)
public class DefaultApiLogInterceptor extends ApiLogInterceptor implements InitializingBean {

    /**
     * 日志格式可以配置
     * 是否打印哪些内容可以配置
     */
    @Value("${person.crayon.tool.log.interceptor.format-template:{0} | {1} | {2} | {3} ms ===> {4}}")
    private String formatTemplate;

    private MessageFormat messageFormat;

    @Override
    public void afterPropertiesSet() {
        messageFormat = new MessageFormat(formatTemplate);
    }

    @Override
    protected void doSomething(ApiLog apiLog) {
        log.info(messageFormat.format(new Object[]{DateUtil.formatHttpDate(DateUtil.date(apiLog.getStartTime())), apiLog.getMethod(), apiLog.getUri(), apiLog.costTime(), apiLog.getResponse()}));
    }
}
