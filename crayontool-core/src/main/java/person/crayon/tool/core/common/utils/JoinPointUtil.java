package person.crayon.tool.core.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author Crayon
 * @date 2022/5/31 19:51
 * 切点工具类
 */
public class JoinPointUtil {
    private JoinPointUtil() {}

    /**
     * 从切点中获取方法信息
     * @param joinPoint 切点
     * @return 方法信息类
     */
    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
