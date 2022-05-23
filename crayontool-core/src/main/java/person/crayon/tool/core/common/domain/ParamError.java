package person.crayon.tool.core.common.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Crayon
 * @date 2022/5/23 8:03
 * 参数校验参数错误属性类
 */
@Data
@Accessors(chain = true)
@ToString
public class ParamError {
    private String name;
    private String message;

    public ParamError(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
