package person.crayon.tool.core.validation.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Crayon
 * @date 2022/5/21 23:05
 * 自定义整型枚举类型校验
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { EnumIntValueValidator.class })
public @interface EnumIntValue {
    int[] enums() default {};

    String message() default "person.crayon.tool.validation.EnumIntValue.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
