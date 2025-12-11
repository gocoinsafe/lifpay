package org.hcm.lifpay.misc.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.FormInfoRequest;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Lifpay短信服务", tags = "Lifpay短信服务")
@Slf4j
@RequestMapping("/misc")
public class SMSController {

    @Autowired



    /**
     * 获取短信或邮箱验证码
     * */
    @ApiOperation(value = "获取短信或邮箱验证码")
    @PostMapping(path = "/verify/code/get")
    public BaseResponse<String> verifyCodeGet(@RequestBody GetVerifyCodeReq request) {
        log.info("收到表单提交请求: {}", request);
        return formService.submitForm(request);
    }



}
