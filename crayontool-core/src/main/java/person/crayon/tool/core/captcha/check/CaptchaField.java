package person.crayon.tool.core.captcha.check;

import java.lang.annotation.*;

/**
 * @author Crayon
 * @date 2022/6/1 10:51
 * 标识验证码属性
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaptchaField {
}
