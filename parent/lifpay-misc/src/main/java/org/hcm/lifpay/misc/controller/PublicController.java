package org.hcm.lifpay.misc.controller;



import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.dto.resp.ExchangeRateModel;
import org.hcm.lifpay.misc.dto.resp.RateModel;
import org.hcm.lifpay.misc.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@Api(value = "Lifpay短信服务", tags = "Lifpay短信服务")
@Slf4j
@RequestMapping("/api/misc")
@RestController
public class PublicController {

    @Autowired
    PublicService publicService;


    /**
     * 获取短信或邮箱验证码
     * */
    @Operation(summary = "获取短信或邮箱验证码")
    @PostMapping(path = "/get/verify/code")
    public BaseResponse<String> getVerifyCode(@RequestBody GetVerifyCodeReq request) {
        log.info("收到获取验证码的请求: {}", request);
        return publicService.getVerifyCode(request);
    }


    /**
     * 获取汇率接口
     * */
    @Operation(summary = "获取汇率接口")
    @PostMapping(path = "/get/exchange/rate")
    public BaseResponse<ExchangeRateModel> getExchangeRate(@RequestBody BaseRequest request) {
        log.info("getExchangeRate: {}", request);
        return publicService.getExchangeRate(request);
    }


}
