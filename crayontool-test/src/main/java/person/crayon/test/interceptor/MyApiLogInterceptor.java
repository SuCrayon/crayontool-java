package person.crayon.test.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import person.crayon.tool.core.common.domain.ApiLog;
import person.crayon.tool.core.log.interceptor.ApiLogInterceptor;

/**
 * @author Crayon
 * @date 2022/5/21 23:11
 * 重写日志打印拦截器
 */
@Slf4j
//@Component
public class MyApiLogInterceptor extends ApiLogInterceptor {
    private static final String FORMAT_TEMPLATE = "{} | {} | {} | {}ms";

    @Override
    public void doSomething(ApiLog apiLog) {
        log.info(StrUtil.format(FORMAT_TEMPLATE, DateUtil.formatHttpDate(DateUtil.date(apiLog.getStartTime())), apiLog.getMethod(), apiLog.getUri(), apiLog.costTime()));
    }
}
