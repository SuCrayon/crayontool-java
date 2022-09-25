package person.crayon.test.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.Data;
import lombok.experimental.Accessors;
import person.crayon.tool.core.desensitization.DesensitizeField;

/**
 * @author Crayon
 * @date 2022/9/25 13:14
 */
@Data
@Accessors(chain = true)
public class UserInfo {
    @DesensitizeField(type = DesensitizedType.EMAIL)
    private String email;
    @DesensitizeField
    private UserInfoInner inner;
    @DesensitizeField
    private List<UserInfoInner> userInfoInnerList;
    @DesensitizeField(type = DesensitizedType.ADDRESS)
    private List<String> addressList;
    @DesensitizeField(type = DesensitizedType.ADDRESS)
    private Map<String, String> addressMap;
    @DesensitizeField
    private Map<String, UserInfoInner> userInfoInnerMap;

    @Data
    @Accessors(chain = true)
    public static class UserInfoInner {
        @DesensitizeField(type = DesensitizedType.MOBILE_PHONE)
        private String mobilePhone;
        @DesensitizeField(type = DesensitizedType.USER_ID)
        private String userID;
    }
}
