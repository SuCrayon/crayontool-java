package person.crayon.test.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Crayon
 * @date 2022/5/23 15:41
 */
@Data
public class Params {
    @NotNull
    private String string;
    private Integer integer;
}
