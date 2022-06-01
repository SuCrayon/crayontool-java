package person.crayon.tool.core.captcha;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

/**
 * @author Crayon
 * @date 2022/5/21 23:36
 * 验证码抽象类，定义了公共的属性、方法
 */
@Data
public abstract class Captcha {
    private String key;
    private String value;

    public Captcha() {
        // 生成uuid
        this.key = IdUtil.fastSimpleUUID();
    }

    public Captcha(String key) {
        this.key = key;
    }

    public abstract boolean compare(String input);
}
