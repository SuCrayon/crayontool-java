package person.crayon.tool.core.log.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import person.crayon.tool.core.common.domain.ApiLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Crayon
 * @date 2022/5/21 22:50
 * 接口日志打印拦截器
 */
@Slf4j
public abstract class ApiLogInterceptor implements HandlerInterceptor {

    /*
    日志格式可以配置
    是否打印哪些内容可以配置
     */

    private final ThreadLocal<ApiLog> LOG_THREADLOCAL = new ThreadLocal<>();

    /**
     * 拦截器最终会调用该方法，你可以在里面自定义一些行为
     *
     * @param apiLog 日志信息类
     */
    protected abstract void doSomething(ApiLog apiLog);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("===> api-log-interceptor");
        LOG_THREADLOCAL.set(
                new ApiLog()
                        .setStartTime(System.currentTimeMillis())
                        .setMethod(request.getMethod())
                        .setUri(request.getRequestURI())
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ApiLog apiLog = LOG_THREADLOCAL.get();
        apiLog.setEndTime(System.currentTimeMillis());
        // TODO: 获取返回结果并设置到apiLog对象中
        doSomething(apiLog);
        // 移除避免内存泄露
        LOG_THREADLOCAL.remove();
    }
}
