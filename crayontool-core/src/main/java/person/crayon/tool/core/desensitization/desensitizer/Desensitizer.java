package person.crayon.tool.core.desensitization.desensitizer;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * @author Crayon
 * @date 2022/10/1 17:17
 */
public interface Desensitizer {
    boolean canAssign(Field field);
    void desensitize(DesensitizeContext ctx);

    @Data
    @Accessors(chain = true)
    class DesensitizeContext {
        private Object obj;
        private Field field;
        private DesensitizedType desensitizedType;
        private Consumer<Object> rb;
    }
}
