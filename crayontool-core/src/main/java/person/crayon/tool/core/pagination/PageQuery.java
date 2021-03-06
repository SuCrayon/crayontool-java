package person.crayon.tool.core.pagination;

import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;

/**
 * @author Crayon
 * @date 2022/5/21 23:27
 * 接口分页查询通用接收类
 */
@Data
@ToString
public class PageQuery<T> {
    private int current;
    private int size;
    @Valid
    private T condition;
}
