package person.crayon.tool.core.response;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Crayon
 * @date 2022/5/21 22:39
 * 响应状态类
 */
@Getter
@ToString
public class ResponseStatus {
    private final String code;
    private final String message;

    public ResponseStatus(Object code, String message) {
        this.code = code.toString();
        this.message = message;
    }
}
