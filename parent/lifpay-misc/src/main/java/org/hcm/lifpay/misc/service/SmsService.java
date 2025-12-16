package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.req.InnerGetVerifyCodeReq;
import org.hcm.lifpay.misc.resp.GetVerifyCodeResp;

public interface SmsService {


    BaseResponse<GetVerifyCodeResp> smsCodeVerify(InnerGetVerifyCodeReq request);


}
