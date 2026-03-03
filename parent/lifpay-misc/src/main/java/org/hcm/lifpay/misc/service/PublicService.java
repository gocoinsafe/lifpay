package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.GetVerifyCodeReq;
import org.hcm.lifpay.misc.dto.resp.ExchangeRateModel;
import org.hcm.lifpay.misc.dto.resp.RateModel;

public interface PublicService {

    /**
     * 获取短信或邮箱验证码
     *
     * */
    BaseResponse<String> getVerifyCode(GetVerifyCodeReq req);


    BaseResponse<ExchangeRateModel> getExchangeRate(BaseRequest request);
}
