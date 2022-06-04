package person.crayon.tool.core.exception.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.response.ApiResponse;

/**
 * @author Crayon
 * @date 2022/5/22 12:28
 * 自定义异常处理器
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ApiResponse handle(CustomException e) {
        return new ApiResponse(e.getStatus()).setData(e.getData());
    }
}
