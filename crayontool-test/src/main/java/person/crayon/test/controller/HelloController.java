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
import person.crayon.test.service.DesensitizeService;
import person.crayon.tool.core.captcha.check.CaptchaField;
import person.crayon.tool.core.captcha.check.CheckCaptcha;
import person.crayon.tool.core.desensitization.Desensitize;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.i18n.I18nUtil;
import person.crayon.tool.core.response.ApiResponse;
import person.crayon.tool.core.response.ApiResult;
import person.crayon.tool.core.validation.constraint.EnumIntValue;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
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

    @Resource
    private DesensitizeService desensitizeService;

    @Resource
    private I18nUtil i18nUtil;

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

    @GetMapping("/desensitization")
    public ApiResponse desensitization() {
        /*return new ApiResult<UserInfo>(StatusConstants.SUCCESS).setData(
                desensitizeService.getUserInfo()
        );*/
        return new ApiResponse().setStatus(StatusConstants.SUCCESS).assignPut(
                desensitizeService.getUserInfo()
        );
    }

    @GetMapping("/i18n")
    public ApiResponse i18n(
            @RequestParam("count") int count
    ) {
        return new ApiResponse().setStatus(StatusConstants.SUCCESS).putItem("message", i18nUtil.getMessageWithPluralSyntax("person.crayon.tool.test.hello.pluralSyntax.1", Locale.US ,count));
    }
}
