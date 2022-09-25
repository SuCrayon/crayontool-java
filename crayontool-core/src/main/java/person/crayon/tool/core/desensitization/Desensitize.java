package person.crayon.tool.core.desensitization;

import java.lang.annotation.*;

/**
 * @author Crayon
 * @date 2022/5/31 19:51
 * 脱敏注解
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitize {
}
