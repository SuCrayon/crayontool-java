package person.crayon.tool.core.i18n.plural;

import org.junit.Test;
import person.crayon.tool.core.i18n.plural.impl.PluralSyntaxParserImpl;

/**
 * @author Crayon
 * @date 2022/10/29 22:07
 */
public class PluralSyntaxParserTest {

    private static final PluralSyntaxParser parser = new PluralSyntaxParserImpl();

    @Test
    public void test() {
        String s = "there ${\"[0]==1\": \"is\", \"default\": \"are\"} \\${\"default\": \"value_one\"} \\\\${\"default\": \"value_two\"}";
        String ret = parser.parse(s, 1);
        System.out.println(ret);
    }

    @Test
    public void testEscape() {
        String s = "\\${\"default\": \"value_one\"}";
        String ret = parser.parse(s, 1);
        System.out.println(ret);
    }

    @Test
    public void testDoubleEscape() {
        String s = "\\\\${\"default\": \"value_two\"}";
        String ret = parser.parse(s, 1);
        System.out.println(ret);
    }
}
