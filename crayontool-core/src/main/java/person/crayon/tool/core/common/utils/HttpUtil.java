package person.crayon.tool.core.common.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Function;

/**
 * @author Crayon
 * @date 2022/5/31 19:51
 * Http工具类
 */
public class HttpUtil {

    private HttpUtil() {}

    /**
     * 获取request对象，非请求域调用则会返回null
     * @return HttpServletRequest/null
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 获取request对象的包装器
     * @param function 内部需要调用request对象函数
     * @param <T> 返回值
     * @return 返回值
     */
    private static <T> T getRequestWrapper(Function<HttpServletRequest, T> function) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return function.apply(request);
    }

    /**
     * 获取Cookie
     * @param key 键
     * @return Cookie
     */
    public static Cookie getCookie(String key) {
        return getRequestWrapper((request) -> {
            for (Cookie cookie : request.getCookies()) {
                if (StrUtil.equals(cookie.getName(), key)) {
                    return cookie;
                }
            }
            return null;
        });
    }

    /**
     * 获取指定Cookie的值
     * @param key 键
     * @return Cookie的值
     */
    public static String getCookieValue(String key) {
        Cookie cookie = getCookie(key);
        return cookie == null ? null : cookie.getValue();
    }

    /**
     * 获取指定Header的值
     * @param key 键
     * @return Header的值
     */
    public static String getHeaderValue(String key) {
        return getRequestWrapper((request) -> request.getHeader(key));
    }

    public static void setHeaderValue(HttpServletResponse response, String key, Object value) {
        response.setHeader(key, value.toString());
    }

    /**
     * 获取真实ip
     * @return ip
     */
    public static String getRealIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        // 这个一般是Nginx反向代理设置的参数
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        if (ip != null && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            ip = ipArray[0];
        }
        return ip;
    }

}
