package person.crayon.tool.core.captcha.check.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import person.crayon.tool.core.captcha.check.CaptchaField;
import person.crayon.tool.core.common.utils.JoinPointUtil;
import person.crayon.tool.core.exception.CustomException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @author Crayon
 * @date 2022/5/31 19:51
 * 验证码校验切面类
 */
@Slf4j
@Aspect
public abstract class CheckCaptchaAspect {

    protected abstract void check(Object input);

    /**
     * 获取验证码属性的值
     * @param joinPoint 切点对象
     * @return 验证码值
     */
    private Object getCaptchaFieldValue(JoinPoint joinPoint) {
        Method method = JoinPointUtil.getMethod(joinPoint);
        Parameter[] parameters = method.getParameters();
        Object fieldValue = null;
        String fieldName= null;
        OUTER_FOR:
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(CaptchaField.class)) {
                fieldName = parameters[i].getName();
                fieldValue = joinPoint.getArgs()[i];
                break;
            }
            Field[] fields = parameters[i].getType().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(CaptchaField.class)) {
                    fieldName = field.getName();
                    fieldValue = ReflectUtil.getFieldValue(joinPoint.getArgs()[i], field);
                    break OUTER_FOR;
                }
            }
        }
        log.debug("captcha fieldName: {}", fieldName);
        return fieldValue;
    }

    /**
     * 前置增强
     * @param joinPoint
     */
    @Before(value = "@annotation(person.crayon.tool.core.captcha.check.CheckCaptcha)")
    private void before(JoinPoint joinPoint) {
        log.debug("CheckCaptchaAspect-before ===>");
        Object fieldValue = getCaptchaFieldValue(joinPoint);
        check(fieldValue);
    }
}
