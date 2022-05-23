package person.crayon.test.constants;

import person.crayon.tool.core.response.ResponseStatus;

/**
 * @author Crayon
 * @date 2022/5/22 12:07
 */
public class StatusConstants {
    public static final ResponseStatus SUCCESS = new ResponseStatus(200, "请求成功");

    public static final ResponseStatus EXCEPTION = new ResponseStatus(500, "异常发生");
}
