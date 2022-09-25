package person.crayon.tool.core.desensitization;

import java.lang.annotation.*;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;

/**
 * @author Crayon
 * @date 2022/9/25 12:20
 * 标识脱敏属性
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DesensitizeField {
    /**
     * 脱敏类型
     * hutool工具类中的脱敏类型
     * 默认是password类型的全脱敏
     */
    DesensitizedType type() default DesensitizedType.PASSWORD;
}
