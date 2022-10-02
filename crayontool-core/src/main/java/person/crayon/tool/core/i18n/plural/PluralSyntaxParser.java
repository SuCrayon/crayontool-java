package person.crayon.tool.core.i18n.plural;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Crayon
 * @date 2022/10/1 20:31
 * 复数语法解析器
 * there ${"[0]==1": "is", "default": "are"} {0} ${"[0]==0": "children", "[0]==1": "child", "[0]>=2": "children", "default": "child"} on the playground.
 */
public interface PluralSyntaxParser {
    @Data
    @Accessors(chain = true)
    @ToString
    class Tuple {
        private State state = State.NONE_FIND;
        private int startIndex;
        private int endIndex;
    }

    enum State {
        NONE_FIND,
        START,
        QUOTES,
        END
    }

    class PluralSyntaxException extends RuntimeException {
        public PluralSyntaxException(String message) {
            super(message);
        }
    }

    List<Tuple> parse2Tuple(String input);

    String standardize(String s);

    List<String> extract(String input, List<Tuple> tuples);

    List<JSONObject> syntaxStr2Obj(List<String> strings);

    List<String> keyExpEval(List<JSONObject> objs, Object... args);

    String replace(String input, List<Tuple> tuples, List<String> values);

    String parse(String input, Object... args);
}
