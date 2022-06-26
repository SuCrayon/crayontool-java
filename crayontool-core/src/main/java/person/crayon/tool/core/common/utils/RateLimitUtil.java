package person.crayon.tool.core.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import person.crayon.tool.core.common.domain.ratelimit.LimitRule;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    public static List<LimitRule> loadLimitRuleFile(@NotNull String path) {
        if (!FileUtil.exist(path)) {
            return Collections.emptyList();
        }
        String str = ResourceUtil.readUtf8Str(path);
        JSONArray jsonArray = JSONUtil.parseArray(str);
        return jsonArray.toList(LimitRule.class);
    }

    public static String getThrottleIdWithIp(String requestPath) {
        return getThrottleIdWithIp(THROTTLE_ID_FORMAT, requestPath, HttpUtil.getRealIp());
    }

    public static String getThrottleIdWithIp(String format, String requestPath, String... args) {
        return StrUtil.format(format, requestPath, args);
    }

    public static String getRequestPathWithMethod(@NotNull HttpServletRequest request) {
        return StrUtil.format(REQUEST_PATH_FORMAT, request.getMethod().toUpperCase(Locale.ENGLISH), request.getRequestURI());
    }
}
