package org.hcm.lifpay.misc;


import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.config.ServiceFeignConfiguration;
import org.hcm.lifpay.misc.req.InnerGetVerifyCodeReq;
import org.hcm.lifpay.misc.resp.GetVerifyCodeResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * misc服务接口
 *
 * @author xinzhe
 */
//@FeignClient
public interface MiscClient {



    /**
     * 获取登录验证码
     *
     * @param request 请求参数
     * @return 验证码
     */
    @PostMapping(path = {"/inner/misc/get/verifyCode"})
    BaseResponse<GetVerifyCodeResp> getVerifyCode(@Valid @RequestBody InnerGetVerifyCodeReq request);


}
