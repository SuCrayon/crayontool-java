package person.crayon.tool.core.exception;

import lombok.Getter;
import person.crayon.tool.core.response.ResponseStatus;

/**
 * @author Crayon
 * @date 2022/5/21 22:46
 * 自定义运行时异常
 */
@Getter
public class CustomException extends RuntimeException {
    private final ResponseStatus status;

    public CustomException(ResponseStatus status) {
        super(status.toString(), null);
        this.status = status;
    }

    public CustomException(ResponseStatus status, Throwable t) {
        super(status.toString(), t);
        this.status = status;
    }
}
