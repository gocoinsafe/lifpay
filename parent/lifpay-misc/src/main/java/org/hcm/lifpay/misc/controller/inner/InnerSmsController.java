package org.hcm.lifpay.misc.controller.inner;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.resp.GetVerifyCodeResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 短信内部控制器
 *
 * @author xinzhe
 */
@Controller
@RequestMapping(path = "/inner/misc", consumes = "application/json")
@Api(value = "短信内部接口", tags = {"短信内部接口"})
@Slf4j
public class InnerSmsController {



    /**
     * 验证短信验证码
     *
     * @param request 参数
     * @return 返回
     */
    @PostMapping(path = {"/get/verifyCode"})
    @ApiOperation(value = "验证短信验证码", notes = "验证短信验证码")
    @ApiImplicitParam(value = "请求参数", required = true, dataType = "SmsCodeVerifyRequest", name = "request")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success message")})
    @ResponseBody
    BaseResponse<GetVerifyCodeResp> smsCodeVerify(@Valid @RequestBody GetVerifyCodeReq request) {
        String logPrefix = "smsCodeVerify";
        log.info("{} request params: {}", logPrefix, JSON.toJSONString(request));
        BaseResponse<GetVerifyCodeResp> result = smsService.smsCodeVerify(request);
        log.info("{} response params: {}", logPrefix, JSON.toJSONString(result));
        return result;
    }
}
