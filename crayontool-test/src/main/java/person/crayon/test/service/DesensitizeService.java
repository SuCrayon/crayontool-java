package person.crayon.test.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import person.crayon.test.domain.UserInfo;
import person.crayon.tool.core.desensitization.Desensitize;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DesensitizeService {

    @Desensitize
    public UserInfo getUserInfo() {
        List<String> addressList = ListUtil.of("神奈川县立湘北高等学校");
        List<UserInfo.UserInfoInner> userInfoInnerList = ListUtil.of(new UserInfo.UserInfoInner().setMobilePhone("13512345678").setUserID("10001002"));
        Map<String, String> addressMap = MapUtil.newHashMap();
        addressMap.put("湘北", "神奈川县立湘北高等学校");
        addressMap.put("山王工业", "秋田县县立山王工业高等学校");
        Map<String, UserInfo.UserInfoInner> userInfoInnerMap = MapUtil.newHashMap();
        userInfoInnerMap.put("one", new UserInfo.UserInfoInner().setMobilePhone("13812345678"));
        userInfoInnerMap.put("two", new UserInfo.UserInfoInner().setMobilePhone("13887654321"));
        return new UserInfo().setEmail("614812345@qq.com").setInner(
                new UserInfo.UserInfoInner().setMobilePhone("13823301234").setUserID("10001001")
        ).setAddressList(addressList).setUserInfoInnerList(userInfoInnerList).setAddressMap(addressMap).setUserInfoInnerMap(userInfoInnerMap);
    }
}
