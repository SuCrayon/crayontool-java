package person.crayon.tool.core.captcha.check;

import java.lang.annotation.*;

/**
 * @author Crayon
 * @date 2022/5/31 19:51
 * 验证码校验注解
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckCaptcha {
}
