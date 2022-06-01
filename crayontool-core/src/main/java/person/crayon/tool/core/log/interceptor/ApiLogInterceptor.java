package person.crayon.tool.core.log.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import person.crayon.tool.core.common.domain.ApiLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Crayon
 * @date 2022/5/21 22:50
 * 接口日志打印拦截器
 * 统一封装了请求时间、请求method、请求uri、接口响应时间、返回结果等
 * eg. 2022/5/21 23:22:00 【GET】 /api/get 100ms ===> { ... }
 */
@Slf4j
public class ApiLogInterceptor implements HandlerInterceptor {

    /*
    日志格式可以配置
    是否打印哪些内容可以配置
     */

    private final ThreadLocal<ApiLog> logThreadLocal = new ThreadLocal<>();

    private static final String FORMAT_TEMPLATE = "{} | {} | {} | {} ms ===> {}";

    /**
     * 拦截器最终会调用该方法，你可以在里面自定义一些行为
     *
     * @param apiLog 日志信息类
     */
    public void doSomething(ApiLog apiLog) {
        log.info(StrUtil.format(FORMAT_TEMPLATE, DateUtil.formatHttpDate(DateUtil.date(apiLog.getStartTime())), apiLog.getMethod(), apiLog.getUri(), apiLog.costTime(), apiLog.getResponse()));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logThreadLocal.set(
                new ApiLog()
                        .setStartTime(System.currentTimeMillis())
                        .setMethod(request.getMethod())
                        .setUri(request.getRequestURI())
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ApiLog apiLog = logThreadLocal.get();
        apiLog.setEndTime(System.currentTimeMillis());
        // TODO: 获取返回结果并设置到apiLog对象中
        doSomething(apiLog);
        // 移除避免内存泄露
        logThreadLocal.remove();
    }
}
