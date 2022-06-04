package person.crayon.tool.core.exception;

import lombok.Getter;
import person.crayon.tool.core.response.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Crayon
 * @date 2022/5/21 22:46
 * 自定义运行时异常
 */
@Getter
public class CustomException extends RuntimeException {
    private final ResponseStatus status;
    private Map<String, Object> data;

    public CustomException(ResponseStatus status) {
        super(status.toString(), null);
        this.status = status;
    }

    public CustomException(ResponseStatus status, Throwable t) {
        super(status.toString(), t);
        this.status = status;
    }

    private void initDataIfNeed() {
        if (this.data == null) {
            synchronized (this) {
                if (this.data == null) {
                    this.data = new HashMap<>();
                }
            }
        }
    }

    public CustomException appendInfo(String key, Object object) {
        initDataIfNeed();
        this.data.put(key, object);
        return this;
    }

    public Map<String, Object> getData() {
        initDataIfNeed();
        return this.data;
    }
}
