package person.crayon.tool.core.captcha;

import lombok.Data;

/**
 * @author Crayon
 * @date 2022/5/21 23:36
 * 验证码抽象类，定义了公共的属性、方法
 */
@Data
public abstract class Captcha {
    private String key;

    public Captcha() {
        this.key = "random key";
    }

    public Captcha(String key) {
        this.key = key;
    }

    public boolean compare() {
        return true;
    }
}
