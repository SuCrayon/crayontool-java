package person.crayon.tool.core.exception.handler;

import cn.hutool.core.collection.ListUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import person.crayon.tool.core.common.domain.ParamError;
import person.crayon.tool.core.i18n.I18nUtil;
import person.crayon.tool.core.response.ApiResponse;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Crayon
 * @date 2022/5/21 23:11
 * 参数校验异常处理器
 */
@RestControllerAdvice
public class ConstraintExceptionHandler implements InitializingBean {

    /**
     * data中字段错误所在key
     * data: {
     *     "data-key": [...]
     * }
     */
    @Value("${person.crayon.tool.exception.handler.constraint.data-key:fields}")
    private String dataKey;

    /**
     * ApiResponse code
     */
    @Value("${person.crayon.tool.exception.handler.constraint.code:400}")
    private String code;

    /**
     * ApiResponse message
     */
    @Value("${person.crayon.tool.exception.handler.constraint.message:Bad Request}")
    private String message;

    /**
     * ApiResponse原型
     */
    private ApiResponse prototype;

    /**
     * i18n工具类
     */
    @Resource
    private I18nUtil i18nUtil;

    @Override
    public void afterPropertiesSet() {
        prototype = new ApiResponse();
        prototype.setCode(code);
        prototype.setMessage(message);
    }

    /**
     * 接口参数有缺失
     *
     * @param e 异常
     * @return ApiResponse
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ApiResponse handle(MissingServletRequestParameterException e) {
        return new ApiResponse().putItem(dataKey, ListUtil.of(new ParamError(e.getParameterName(), e.getMessage())));
    }

    /**
     * Validated注解加在类上
     * @param e 异常
     * @return ApiResponse
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ApiResponse handle(ConstraintViolationException e) {
        List<ParamError> res = new ArrayList<>(e.getConstraintViolations().size());
        e.getConstraintViolations().forEach(err -> res.add(new ParamError(err.getPropertyPath().toString(), err.getMessage())));
        return new ApiResponse().putItem(dataKey, res);
    }

    @ExceptionHandler({BindException.class})
    public ApiResponse handle(BindException e) {
        List<ParamError> res = new ArrayList<>(e.getBindingResult().getFieldErrorCount());
        e.getBindingResult().getFieldErrors().forEach(err -> res.add(new ParamError(err.getField(), i18nUtil.tryGetMessage(err.getDefaultMessage()))));
        return new ApiResponse().putItem(dataKey, res);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponse handle(MethodArgumentNotValidException e) {
        List<ParamError> res = new ArrayList<>(e.getBindingResult().getFieldErrorCount());
        e.getBindingResult().getFieldErrors().forEach(err -> res.add(new ParamError(err.getField(), err.getDefaultMessage())));
        return new ApiResponse().putItem(dataKey, res);
    }

    /**
     * 参数类型不匹配
     * @param e 异常
     * @return ApiResponse
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ApiResponse handle(MethodArgumentTypeMismatchException e) {
        return new ApiResponse().putItem(dataKey, new ParamError(e.getName(), e.getMessage()));
    }

    /**
     * RequestBody缺失
     * JSON转换失败，如参数类型不匹配，在转换时出错
     * @param e 异常
     * @return ApiResponse
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ApiResponse handle(HttpMessageNotReadableException e) {
        return new ApiResponse().setMessage(e.getMessage());
    }
}
