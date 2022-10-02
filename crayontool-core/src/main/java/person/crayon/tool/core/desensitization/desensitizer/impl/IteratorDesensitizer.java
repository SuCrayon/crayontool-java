package person.crayon.tool.core.desensitization.desensitizer.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.desensitization.desensitizer.Desensitizer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * @author Crayon
 * @date 2022/10/1 17:23
 */
@Slf4j
public class IteratorDesensitizer implements Desensitizer {
    @Override
    public boolean canAssign(Field field) {
        return List.class.isAssignableFrom(field.getType())
                || Queue.class.isAssignableFrom(field.getType());
    }

    @Override
    public void desensitize(DesensitizeContext ctx) {
        log.debug("meet List|Queue type, fieldName: {}", ctx.getField().getName());
        // List或Queue类型的子类
        Collection<Object> originCollection = (Collection<Object>) ReflectUtil.getFieldValue(ctx.getObj(), ctx.getField());
        if (CollectionUtil.isEmpty(originCollection)) {
            return;
        }
        Collection<Object> retCollection = (Collection<Object>) ReflectUtil.newInstanceIfPossible(ctx.getField().getType());
        for (Object value : originCollection) {
            if (value.getClass() == String.class) {
                // 字符串类型
                retCollection.add(
                        DesensitizedUtil.desensitized((String) value, ctx.getDesensitizedType())
                );
                continue;
            }
            // 上下文递归
            ctx.getRb().accept(value);
            retCollection.add(value);
        }
        // 设置新的脱敏后的结果集
        ReflectUtil.setFieldValue(ctx.getObj(), ctx.getField(), retCollection);
    }
}
