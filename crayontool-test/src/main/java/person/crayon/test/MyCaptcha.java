package person.crayon.test;

import org.springframework.stereotype.Component;
import person.crayon.tool.core.captcha.check.aspect.CheckCaptchaAspect;

@Component
public class MyCaptcha extends CheckCaptchaAspect {
    @Override
    protected void check(Object input) {
        System.out.println("input: " + input);
    }
}
