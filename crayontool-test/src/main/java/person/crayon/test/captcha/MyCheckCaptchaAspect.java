package person.crayon.test.captcha;

import org.springframework.stereotype.Component;
import person.crayon.tool.core.captcha.check.aspect.CheckCaptchaAspect;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.response.ResponseStatus;

/**
 * @author Crayon
 * @date 2022/6/4 19:31
 */
@Component
public class MyCheckCaptchaAspect extends CheckCaptchaAspect {
    @Override
    protected void check(Object input) {
        if (!"123456".equals(input)) {
            throw new CustomException(new ResponseStatus("400", "验证码不正确"));
        }
    }
}
