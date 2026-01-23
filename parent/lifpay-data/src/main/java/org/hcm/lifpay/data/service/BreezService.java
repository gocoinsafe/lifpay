package org.hcm.lifpay.data.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.data.dto.req.LnurlPayIdentifierReq;
import org.hcm.lifpay.data.dto.req.LnurlPayInvoiceReq;
import org.hcm.lifpay.data.dto.req.RegisterLnUrlWebhookReq;
import org.hcm.lifpay.data.dto.req.WebhookCallbackReq;

public interface BreezService {


    /**
     * Webhook 注册
     * */
    BaseResponse<String> breezRegisterLnUrlWebHook(RegisterLnUrlWebhookReq request);


    /**
     * 取消Webhook 注册
     * */
    BaseResponse<String> breezUnregisterLnUrlWebHook(RegisterLnUrlWebhookReq request);



    /**
     * 恢复已注册的lnurl和闪电地址
     * */
    BaseResponse<String> breezLnurlPayRecover(RegisterLnUrlWebhookReq request);

    /**
     * LNURL支付信息端点
     * */
    BaseResponse<String> breezLnurlPayIdentifier(LnurlPayIdentifierReq request);

    /**
     * LNURL 付款接口
     * */
    BaseResponse<String> breezLnurlPayInvoice(LnurlPayInvoiceReq request);



    BaseResponse<String> breezLnUrlPayResponse(WebhookCallbackReq request);

}
