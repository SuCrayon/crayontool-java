package person.crayon.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import person.crayon.test.constants.StatusConstants;
import person.crayon.test.domain.Params;
import person.crayon.test.domain.UserInfo;
import person.crayon.test.domain.UserInfo.UserInfoInner;
import person.crayon.tool.core.captcha.check.CaptchaField;
import person.crayon.tool.core.captcha.check.CheckCaptcha;
import person.crayon.tool.core.desensitization.Desensitize;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.response.ApiResponse;
import person.crayon.tool.core.response.ApiResult;
import person.crayon.tool.core.validation.constraint.EnumIntValue;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

/**
 * @author Crayon
 * @date 2022/5/22 11:58
 */
@Slf4j
@RestController
@RequestMapping("/hello")
@Validated
public class HelloController {

    @GetMapping
    public ApiResponse get() {
        return new ApiResponse(StatusConstants.SUCCESS);
    }

    @GetMapping("/param")
    public ApiResponse param(
            @RequestParam(name = "p") String p
    ) {
        return new ApiResponse(StatusConstants.SUCCESS);
    }

    @GetMapping("/param0")
    public ApiResponse param0(
            @EnumIntValue(enums = {100, 101}) @RequestParam(name = "p") Integer p,
            @Min(0) @RequestParam(name = "p0") Integer p0
    ) {
        return new ApiResponse(StatusConstants.SUCCESS);
    }

    @PostMapping("/param1")
    public ApiResponse param1(
            @Validated @RequestBody Params params
    ) {
        return new ApiResponse(StatusConstants.SUCCESS);
    }

    @GetMapping("/exception")
    public ApiResponse exception() {
        throw new CustomException(StatusConstants.EXCEPTION);
    }

    @CheckCaptcha
    @PostMapping("/check-captcha")
    public ApiResponse checkCaptcha(
            @RequestBody Params params
    ) {
        return new ApiResponse(StatusConstants.SUCCESS);
    }

    @Desensitize
    @GetMapping("/desensitization")
    public ApiResult<UserInfo> desensitization() {
        List<String> addressList = ListUtil.of("神奈川县立湘北高等学校");
        List<UserInfoInner> userInfoInnerList = ListUtil.of(new UserInfoInner().setMobilePhone("13512345678").setUserID("10001002"));
        Map<String, String> addressMap = MapUtil.newHashMap();
        addressMap.put("湘北", "神奈川县立湘北高等学校");
        addressMap.put("山王工业", "秋田县县立山王工业高等学校");
        Map<String, UserInfoInner> userInfoInnerMap = MapUtil.newHashMap();
        userInfoInnerMap.put("one", new UserInfoInner().setMobilePhone("13812345678"));
        userInfoInnerMap.put("two", new UserInfoInner().setMobilePhone("13887654321"));
        return new ApiResult<UserInfo>(StatusConstants.SUCCESS).setData(
            new UserInfo().setEmail("614812345@qq.com").setInner(
                new UserInfoInner().setMobilePhone("13823301234").setUserID("10001001")
            ).setAddressList(addressList).setUserInfoInnerList(userInfoInnerList).setAddressMap(addressMap).setUserInfoInnerMap(userInfoInnerMap)
        );
    }
}
