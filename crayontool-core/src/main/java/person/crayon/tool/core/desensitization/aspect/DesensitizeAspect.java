package person.crayon.tool.core.desensitization.aspect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.desensitization.DesensitizeField;

/**
 * @author Crayon
 * @date 2022/9/25 12:35
 * 脱敏切面类
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "person.crayon.tool.desensitization", value = "enable", havingValue = "true")
public class DesensitizeAspect implements InitializingBean {

    /**
     * DON'T MODIFY IT AFTER INITIALIZATION!
     */
    private Set<DesensitizedType> desensitizedTypeSet;

    private void recursiveDesensitize(Object result) {
        Class<?> clazz = result.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);
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
                if (field.getType() == String.class) {
                    log.debug("meet String type, fieldName: {}", field.getName());
                    // 只有字符串类型才会做脱敏
                    // 获取原始值
                    String originValue = (String) ReflectUtil.getFieldValue(result, field);
                    // 脱敏
                    String value = DesensitizedUtil.desensitized(originValue, desensitizedType);
                    // 设置脱敏后的值
                    ReflectUtil.setFieldValue(result, field, value);
                } else if (List.class.isAssignableFrom(field.getType())
                        || Queue.class.isAssignableFrom(field.getType())) {
                    log.debug("meet List|Queue type, fieldName: {}", field.getName());
                    // List或Queue类型的子类
                    Collection<Object> originCollection = (Collection<Object>) ReflectUtil.getFieldValue(result, field);
                    if (originCollection == null) {
                        continue;
                    }
                    Collection<Object> retCollection = (Collection<Object>) ReflectUtil.newInstanceIfPossible(field.getType());
                    for (Object value : originCollection) {
                        if (value.getClass() == String.class) {
                            // 字符串类型
                            retCollection.add(
                                DesensitizedUtil.desensitized((String) value, desensitizedType)
                            );
                            continue;
                        }
                        recursiveDesensitize(value);
                        retCollection.add(value);
                    }
                    // 设置新的脱敏后的结果集
                    ReflectUtil.setFieldValue(result, field, retCollection);
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    log.debug("meet Map type, fieldName: {}", field.getName());
                    Map<Object, Object> originMap = (Map<Object, Object>) ReflectUtil.getFieldValue(result, field);
                    if (originMap == null) {
                        continue;
                    }
                    Map<Object, Object> retMap = (Map<Object, Object>) ReflectUtil.newInstanceIfPossible(field.getType());
                    for (Map.Entry<Object, Object> entry : originMap.entrySet()) {
                        if (entry.getValue().getClass() == String.class) {
                            // 字符串类型
                            retMap.put(
                                entry.getKey(),
                                DesensitizedUtil.desensitized((String) entry.getValue(), desensitizedType)
                            );
                            continue;
                        }
                        recursiveDesensitize(entry.getValue());
                        retMap.put(entry.getKey(), entry.getValue());
                    }
                    // 设置新的脱敏后的结果集
                    ReflectUtil.setFieldValue(result, field, retMap);
                } else {
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
        for (DesensitizedType type : types) {
            desensitizedTypeSet.add(type);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDesensitizedTypeSet();
    }
}
