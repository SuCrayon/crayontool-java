package person.crayon.test.domain;

import lombok.Data;
import person.crayon.tool.core.captcha.check.CaptchaField;

import javax.validation.constraints.NotNull;

/**
 * @author Crayon
 * @date 2022/5/23 15:41
 */
@Data
public class Params {
    @CaptchaField
    private String string;
    @NotNull
    private Integer integer;
}
