package person.crayon.tool.core.common.constants;

/**
 * @author Crayon
 * @date 2022/6/2 16:51
 * 拦截器顺序常量类
 */
public class InterceptorOrder {
    /**
     * 接口日志拦截器顺序
     */
    public static final int API_LOG_ORDER = 999;

    /**
     * 限流拦截器顺序
     * 总在接口日志之后
     */
    public static final int RATE_LIMITER_ORDER = API_LOG_ORDER + 1;


}
