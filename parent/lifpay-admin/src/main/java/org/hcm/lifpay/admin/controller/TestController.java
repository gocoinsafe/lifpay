package org.hcm.lifpay.admin.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.admin.dto.SwaggerTest;
import org.hcm.lifpay.common.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Lifpay管理后台")
@Slf4j
@RequestMapping("/api/admin")
public class TestController {


    /**
     * 获取短信或邮箱验证码
     * */
    @Operation(summary = "获取短信或邮箱验证码")
    @PostMapping(path = "/get/verify/code")
    public BaseResponse<String> getVerifyCode(@RequestBody SwaggerTest request) {
        log.info("收到获取验证码的请求: {}", request);
        return new BaseResponse<>();
    }


}
