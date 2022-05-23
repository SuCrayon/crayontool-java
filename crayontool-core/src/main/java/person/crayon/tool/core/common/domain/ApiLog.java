package person.crayon.tool.core.common.domain;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import person.crayon.tool.core.response.ApiResponse;

/**
 * @author Crayon
 * @date 2022/5/23 5:57
 * 接口日志打印属性类
 */
@Data
@Accessors(chain = true)
public class ApiLog {
    private long startTime;
    private long endTime;
    private String uri;
    private String method;
    private ApiResponse response;

    /**
     * 计算耗时
     * @return 耗时
     */
    public final long costTime() {
        return this.endTime - this.startTime;
    }
}
