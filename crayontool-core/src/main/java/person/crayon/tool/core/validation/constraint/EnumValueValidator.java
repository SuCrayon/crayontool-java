package person.crayon.tool.core.validation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Crayon
 * @date 2022/5/22 13:17
 * EnumValue具体校验逻辑类
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
