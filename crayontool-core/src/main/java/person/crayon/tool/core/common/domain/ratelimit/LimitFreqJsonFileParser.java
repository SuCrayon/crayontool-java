package person.crayon.tool.core.common.domain.ratelimit;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/6/4 18:13
 * 限流频率json文件解析器
 * 从json解析得到Map
 */
public class LimitFreqJsonFileParser {
    public static Map<String, LimitFreq> parse(String path) {
        HashMap<String, LimitFreq> res = MapUtil.newHashMap();
        if (StrUtil.isEmpty(path)) {
            return res;
        }
        if (!FileUtil.exist(path)) {
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
}
