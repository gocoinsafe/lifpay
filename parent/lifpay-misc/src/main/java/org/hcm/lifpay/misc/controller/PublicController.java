package org.hcm.lifpay.misc.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Lifpay短信服务", tags = "Lifpay短信服务")
@Slf4j
@RequestMapping("/api/misc")
public class PublicController {

    @Autowired
    PublicService publicService;


    /**
     * 获取短信或邮箱验证码
     * */
    @ApiOperation(value = "获取短信或邮箱验证码")
    @PostMapping(path = "/get/verify/code")
    public BaseResponse<String> getVerifyCode(@RequestBody GetVerifyCodeReq request) {
        log.info("收到获取验证码的请求: {}", request);
        return publicService.getVerifyCode(request);
    }



}
