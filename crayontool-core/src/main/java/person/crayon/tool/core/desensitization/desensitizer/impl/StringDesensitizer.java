package person.crayon.tool.core.desensitization.desensitizer.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.desensitization.desensitizer.Desensitizer;

import java.lang.reflect.Field;

/**
 * @author Crayon
 * @date 2022/10/1 18:14
 */
@Slf4j
public class StringDesensitizer implements Desensitizer {
    @Override
    public boolean canAssign(Field field) {
        return field.getType() == String.class;
    }

    @Override
    public void desensitize(DesensitizeContext ctx) {
        log.debug("meet String type, fieldName: {}", ctx.getField().getName());
        // 只有字符串类型才会做脱敏
        // 获取原始值
        String originValue = (String) ReflectUtil.getFieldValue(ctx.getObj(), ctx.getField());
        if (StrUtil.isEmpty(originValue)) {
            return;
        }
        // 脱敏
        String value = DesensitizedUtil.desensitized(originValue, ctx.getDesensitizedType());
        // 设置脱敏后的值
        ReflectUtil.setFieldValue(ctx.getObj(), ctx.getField(), value);
    }
}
