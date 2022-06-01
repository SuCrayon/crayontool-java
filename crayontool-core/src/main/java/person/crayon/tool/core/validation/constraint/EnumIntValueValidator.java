package person.crayon.tool.core.validation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Crayon
 * @date 2022/5/22 13:17
 * EnumValue具体校验逻辑类
 */
public class EnumIntValueValidator implements ConstraintValidator<EnumIntValue, Integer> {

    private Set<Integer> set;

    @Override
    public void initialize(EnumIntValue constraintAnnotation) {
        set = new HashSet<>(constraintAnnotation.enums().length);
        for (int i : constraintAnnotation.enums()) {
            set.add(i);
        }
    }

    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(i);
    }
}
