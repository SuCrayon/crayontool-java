package person.crayon.tool.core.response;

import lombok.Getter;
import lombok.ToString;
import person.crayon.tool.core.common.Copyable;

/**
 * @author Crayon
 * @date 2022/5/21 22:39
 * 响应状态类
 */
@Getter
@ToString
public class ResponseStatus implements Copyable<ResponseStatus> {
    private final String code;
    private final String message;

    public ResponseStatus() {
        this.code = "";
        this.message = "";
    }

    public ResponseStatus(Object code, String message) {
        this.code = code.toString();
        this.message = message;
    }

    @Override
    public ResponseStatus copy() {
        return new ResponseStatus(this.code, this.message);
    }
}
