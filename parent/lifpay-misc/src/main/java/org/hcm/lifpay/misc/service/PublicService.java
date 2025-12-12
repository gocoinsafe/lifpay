package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;

public interface PublicService {

    /**
     * 获取短信或邮箱验证码
     *
     * */
    BaseResponse<String> getVerifyCode(GetVerifyCodeReq req);
}
