package person.crayon.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import person.crayon.test.constants.StatusConstants;
import person.crayon.test.domain.Params;
import person.crayon.tool.core.captcha.check.CaptchaField;
import person.crayon.tool.core.captcha.check.CheckCaptcha;
import person.crayon.tool.core.exception.CustomException;
import person.crayon.tool.core.response.ApiResponse;
import person.crayon.tool.core.validation.constraint.EnumIntValue;

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
}
