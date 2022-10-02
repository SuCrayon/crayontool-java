package person.crayon.tool.core.desensitization.aspect;

import java.lang.reflect.Field;
import java.util.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.desensitization.DesensitizeField;
import person.crayon.tool.core.desensitization.desensitizer.Desensitizer;

import javax.annotation.Resource;

/**
 * @author Crayon
 * @date 2022/9/25 12:35
 * 脱敏切面类
 */
@Slf4j
@Aspect
public class DesensitizeAspect implements InitializingBean {

    /**
     * DON'T MODIFY IT AFTER INITIALIZATION!
     */
    private Set<DesensitizedType> desensitizedTypeSet;

    @Resource
    private List<Desensitizer> desensitizers;

    private void recursiveDesensitize(Object result) {
        Class<?> clazz = result.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);
        Desensitizer.DesensitizeContext ctx = new Desensitizer.DesensitizeContext()
                .setObj(result)
                .setRb(this::recursiveDesensitize);
        for (Field field : fields) {
            if (field.isAnnotationPresent(DesensitizeField.class)) {
                // 有脱敏注解，所以是需要脱敏的字段
                if (field.getType().isPrimitive()) {
                    // 基本类型直接跳过
                    continue;
                }
                DesensitizedType desensitizedType = AnnotationUtil.getAnnotationValue(field, DesensitizeField.class,
                        "type");
                if (!desensitizedTypeSet.contains(desensitizedType)) {
                    // 不合法的类型
                    log.warn("not a support desensitized type, desensitizedType: {}", desensitizedType);
                    continue;
                }
                ctx.setField(field);
                ctx.setDesensitizedType(desensitizedType);

                boolean flag = false;
                for (Desensitizer desensitizer : desensitizers) {
                    if (desensitizer.canAssign(field)) {
                        desensitizer.desensitize(ctx);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    log.debug("need to recursive desensitize, fieldName: {}", field.getName());
                    // 递归解析类内部的字段
                    Object innerResult = ReflectUtil.getFieldValue(result, field);
                    recursiveDesensitize(innerResult);
                }
            }
        }
    }

    @AfterReturning(value = "@annotation(person.crayon.tool.core.desensitization.Desensitize)", returning = "result")
    private void afterReturning(JoinPoint joinPoint, Object result) {
        log.debug("===> DesensitizeAspect-afterReturning");
        recursiveDesensitize(result);
    }

    private void initDesensitizedTypeSet() {
        DesensitizedType[] types = DesensitizedType.values();
        desensitizedTypeSet = new HashSet<>(types.length);
        Collections.addAll(desensitizedTypeSet, types);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDesensitizedTypeSet();
    }
}
