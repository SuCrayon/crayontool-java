package person.crayon.tool.core.desensitization.desensitizer.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.desensitization.desensitizer.Desensitizer;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/10/1 17:20
 */
@Slf4j
public class MapDesensitizer implements Desensitizer {
    @Override
    public boolean canAssign(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    @Override
    public void desensitize(DesensitizeContext ctx) {
        log.debug("meet Map type, fieldName: {}", ctx.getField().getName());
        Map<Object, Object> originMap = (Map<Object, Object>) ReflectUtil.getFieldValue(ctx.getObj(), ctx.getField());
        if (MapUtil.isEmpty(originMap)) {
            return;
        }
        Map<Object, Object> retMap = (Map<Object, Object>) ReflectUtil.newInstanceIfPossible(ctx.getField().getType());
        for (Map.Entry<Object, Object> entry : originMap.entrySet()) {
            if (entry.getValue().getClass() == String.class) {
                // 字符串类型
                retMap.put(
                        entry.getKey(),
                        DesensitizedUtil.desensitized((String) entry.getValue(), ctx.getDesensitizedType())
                );
                continue;
            }
            // 上下文递归
            ctx.getRb().accept(entry.getValue());
            retMap.put(entry.getKey(), entry.getValue());
        }
        // 设置新的脱敏后的结果集
        ReflectUtil.setFieldValue(ctx.getObj(), ctx.getField(), retMap);
    }
}
