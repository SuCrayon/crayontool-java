package person.crayon.tool.core.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreq;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/6/12 13:29
 * 限流相关工具类
 */
public class RateLimitUtil {
    private static final String THROTTLE_ID_SEPARATOR = " ";
    private static final String THROTTLE_ID_FORMAT = "{}" + THROTTLE_ID_SEPARATOR + "{}";
    private static final String REQUEST_PATH_SEPARATOR = " ";
    private static final String REQUEST_PATH_FORMAT = "{}" + REQUEST_PATH_SEPARATOR + "{}";

    private RateLimitUtil() {}

    /**
     * 限流频率json文件解析器
     * 从json解析得到Map
     * @param path 文件路径
     * @return map
     */
    public static Map<String, LimitFreq> limitFreqFile2Map(String path) {
        HashMap<String, LimitFreq> res = MapUtil.newHashMap();
        if (StrUtil.isBlank(path) || !FileUtil.exist(path)) {
            return res;
        }
        String str = ResourceUtil.readUtf8Str(path);
        if (StrUtil.isBlank(str)) {
            return res;
        }
        JSONObject jsonObject = JSONUtil.parseObj(str);
        jsonObject.forEach((k, v) -> res.put(k, JSONUtil.parseObj(v).toBean(LimitFreq.class)));
        return res;
    }

    public static String getThrottleIdWithIp(String requestPath) {
        return getThrottleIdWithIp(THROTTLE_ID_FORMAT, requestPath, HttpUtil.getRealIp());
    }

    public static String getThrottleIdWithIp(String format, String requestPath, String... args) {
        return StrUtil.format(format, requestPath, args);
    }

    public static String getRequestPathWithMethod(@org.jetbrains.annotations.NotNull HttpServletRequest request) {
        return StrUtil.format(REQUEST_PATH_FORMAT, request.getMethod().toUpperCase(Locale.ENGLISH), request.getRequestURI());
    }
}
