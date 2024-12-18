package person.crayon.tool.core.i18n.plural.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import person.crayon.tool.core.i18n.plural.PluralSyntaxParser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Crayon
 * @date 2022/10/1 22:58
 * 复数语法解析器
 * there ${"[0]==1": "is", "default": "are"} {0} ${"[0]==0": "children", "[0]==1": "child", "[0]>=2": "children", "default": "child"} on the playground.
 * 解析出${}包裹的表达式，该表达式满足json语法
 * 支持转义，\${}将不作为表达式进行解析
 */
@Slf4j
public class PluralSyntaxParserImpl implements PluralSyntaxParser {

    public static final char ESCAPE = '\\';
    public static final char LEFT_BOUND_START = '$';
    public static final char LEFT_BOUND = '{';
    public static final char RIGHT_BOUND = '}';
    public static final char QUOTES = '"';

    public static final String STANDARDIZE_TEMPLATE = "{%s}";
    public static final String REGEX_ARGS_INDEX = "\\[(.*?)\\]";

    public static final String DEFAULT_KEY_NAME = "default";

    public static final PluralSyntaxException SIZE_NOT_EQUALS = new PluralSyntaxException("tuples and values size not equals, may be more than one key expression are satisfiable or some syntax block have no satisfiable value");
    public static final ArrayIndexOutOfBoundsException INDEX_OUT_OF_BOUNDS = new ArrayIndexOutOfBoundsException("index is out of args bound! may be you forget to pass some argument");

    /**
     * 解析得到tuple
     *
     * @param input
     * @return
     */
    @Override
    public List<Tuple> parse2Tuple(String input) {
        // 1. 遍历字符，找到$
        // 2. 找到{
        // 3. 找到}
        List<Tuple> ret = new LinkedList<>();
        char c;
        Tuple tuple = new Tuple();
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (tuple.getState() == State.NONE_FIND) {
                if (c == LEFT_BOUND_START) {
                    boolean flag = true;
                    if (i > 0 && input.charAt(i - 1) == ESCAPE) {
                        flag = false;
                        // 有转义字符
                        if (i > 1 && input.charAt(i - 2) == ESCAPE) {
                            // 转义字符前面还有转义，则不转义
                            flag = true;
                        }
                    } else {
                        // 没有转义字符
                    }
                    if (flag && i + 2 < input.length() && input.charAt(i + 1) == LEFT_BOUND) {
                        // 不是转义，且下一个是左边界符
                        // i + 2 < input.length() 是因为$后面至少要有这两个字符{}
                        tuple.setState(State.START);
                        tuple.setStartIndex(i);
                    }
                }
            } else if (tuple.getState() == State.START) {
                if (c == RIGHT_BOUND) {
                    // 找到右边界符，加入结果集
                    tuple.setEndIndex(i);
                    tuple.setState(State.END);
                    ret.add(tuple);
                    tuple = new Tuple();
                } else if (c == QUOTES) {
                    tuple.setState(State.QUOTES);
                }
            } else if (tuple.getState() == State.QUOTES) {
                if (c == QUOTES && (i == 0 || input.charAt(i - 1) != ESCAPE)) {
                    // 不是转义的引号
                    tuple.setState(State.START);
                }
            } else if (tuple.getState() == State.END) {

            } else {
                // at least for now there's nothing to do here
            }
        }
        return ret;
    }

    /**
     * 提取出的语法串进行标准化
     *
     * @param s
     * @return
     */
    @Override
    public String standardize(String s) {
        return String.format(STANDARDIZE_TEMPLATE, s);
    }

    /**
     * 根据tuple从原始输入串中提取字符串
     *
     * @param input
     * @param tuples
     * @return
     */
    @Override
    public List<String> extract(String input, List<Tuple> tuples) {
        List<String> ret = new ArrayList<>(tuples.size());
        for (Tuple tuple : tuples) {
            ret.add(standardize(input.substring(tuple.getStartIndex() + 2, tuple.getEndIndex())));
        }
        return ret;
    }

    /**
     * 语法字符串解析为obj
     * @param strings
     * @return
     */
    @Override
    public List<JSONObject> syntaxStr2Obj(List<String> strings) {
        return strings.stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    /**
     * 遍历表达式，选择指定的值
     * @param objs
     * @param args
     * @return
     */
    @Override
    public List<String> keyExpEval(List<JSONObject> objs, Object... args) {
        List<String> retList = new ArrayList<>(objs.size());
        String key;
        Object value;
        SpelExpressionParser expParser = new SpelExpressionParser();
        String ret;
        for (JSONObject obj : objs) {
            ret = null;
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (StrUtil.equalsIgnoreCase(key, DEFAULT_KEY_NAME)) {
                    continue;
                }
                key = ReUtil.replaceAll(key, REGEX_ARGS_INDEX, matcher -> {
                    int index = Integer.parseInt(Optional.ofNullable(matcher.group(1)).orElse("-1"));
                    if (index >= args.length) {
                        // 超出了入参个数
                        throw INDEX_OUT_OF_BOUNDS;
                    }
                    return String.valueOf(args[index]);
                });
                // 表达式判断是否为真，是的话结束
                if (Optional.ofNullable(expParser.parseExpression(key).getValue(boolean.class)).orElse(false)) {
                    ret = String.valueOf(value);
                    break;
                }
            }
            retList.add(ObjectUtil.isNull(ret) ? String.valueOf(obj.getOrDefault(DEFAULT_KEY_NAME, StrUtil.EMPTY)) : ret);
        }
        return retList;
    }

    /**
     * 用解析后的结果替换原表达式
     * @param input
     * @param tuples
     * @param values
     * @return
     */
    @Override
    public String replace(String input, List<Tuple> tuples, List<String> values) {
        if (tuples.size() != values.size()) {
            throw SIZE_NOT_EQUALS;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int i = 0; i < tuples.size(); i++) {
            sb.append(input, index, tuples.get(i).getStartIndex());
            sb.append(values.get(i));
            index = tuples.get(i).getEndIndex() + 1;
        }
        sb.append(input, index, input.length());
        return sb.toString();
    }

    /**
     * 解析复数语法并返回解析后的结果串
     *
     * @param input
     * @return
     */
    @Override
    public String parse(String input, Object... args) {
        List<Tuple> tuples = parse2Tuple(input);
        List<String> strings = extract(input, tuples);
        List<JSONObject> objs = syntaxStr2Obj(strings);
        List<String> values = keyExpEval(objs, args);
        return replace(input, tuples, values);
    }
}
