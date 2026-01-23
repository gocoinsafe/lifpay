package org.hcm.lifpay.data.service.impl;


import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.data.dto.req.LnurlPayIdentifierReq;
import org.hcm.lifpay.data.dto.req.LnurlPayInvoiceReq;
import org.hcm.lifpay.data.dto.req.RegisterLnUrlWebhookReq;
import org.hcm.lifpay.data.dto.req.WebhookCallbackReq;
import org.hcm.lifpay.data.service.BreezService;
import org.springframework.stereotype.Service;

@Service
public class BreezServiceImpl implements BreezService {


    @Override
    public BaseResponse<String> breezRegisterLnUrlWebHook(RegisterLnUrlWebhookReq request) {
        return new BaseResponse<>();
    }


    @Override
    public BaseResponse<String> breezUnregisterLnUrlWebHook(RegisterLnUrlWebhookReq request) {
        return new BaseResponse<>();
    }

    @Override
    public BaseResponse<String> breezLnurlPayRecover(RegisterLnUrlWebhookReq request) {
        return new BaseResponse<>();
    }


    @Override
    public BaseResponse<String> breezLnurlPayIdentifier(LnurlPayIdentifierReq request) {
        return new BaseResponse<>();
    }


    @Override
    public BaseResponse<String> breezLnurlPayInvoice(LnurlPayInvoiceReq request) {
        return new BaseResponse<>();
    }


    @Override
    public BaseResponse<String> breezLnUrlPayResponse(WebhookCallbackReq request) {
        return new BaseResponse<>();
    }



}
