package person.crayon.test.interceptor;

import org.springframework.stereotype.Component;
import person.crayon.tool.core.common.domain.ApiLog;
import person.crayon.tool.core.log.interceptor.ApiLogInterceptor;

/**
 * @author Crayon
 * @date 2022/5/21 23:11
 * 重写日志打印拦截器
 */
//@Component
public class MyApiLogInterceptor extends ApiLogInterceptor {
    @Override
    public void doSomething(ApiLog apiLog) {
        System.out.println(apiLog);
    }
}
