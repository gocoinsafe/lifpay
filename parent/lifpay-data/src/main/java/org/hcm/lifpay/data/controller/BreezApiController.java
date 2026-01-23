package org.hcm.lifpay.data.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.data.dto.req.LnurlPayIdentifierReq;
import org.hcm.lifpay.data.dto.req.LnurlPayInvoiceReq;
import org.hcm.lifpay.data.dto.req.RegisterLnUrlWebhookReq;
import org.hcm.lifpay.data.dto.req.WebhookCallbackReq;
import org.hcm.lifpay.data.dto.resp.LnurlPayIdentifierResp;
import org.hcm.lifpay.data.dto.resp.RegisterLnUrlWebhookResp;
import org.hcm.lifpay.data.service.BreezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





@Tag(name = "Breez节点服务交互接口")
@Slf4j
@RequestMapping("/api/data")
@RestController
public class BreezApiController {


    @Autowired
    BreezService breezService;

    /**
     * 注册 LNURL Webhook
     * */
    @Operation(summary = "注册lnurl Webhook")
    @PostMapping(path = "/breez/register/lnUrl/webHook")
    public BaseResponse<RegisterLnUrlWebhookResp> breezRegisterLnUrlWebHook(@RequestBody RegisterLnUrlWebhookReq request) {
        log.info("breezRegisterLnUrlWebHook: {}", request);
        return breezService.breezRegisterLnUrlWebHook(request);
    }


    @Operation(summary = "取消注册lnurl Webhook")
    @PostMapping(path = "/breez/unregister/lnUrl/webHook")
    public BaseResponse<String> breezUnregisterLnUrlWebHook(@RequestBody RegisterLnUrlWebhookReq request) {
        log.info("breezUnregisterLnUrlWebHook: {}", request);
        return breezService.breezUnregisterLnUrlWebHook(request);
    }


    @Operation(summary = "恢复已注册的lnurl和闪电地址")
    @PostMapping(path = "/breez/ln-url-pay/recover")
    public BaseResponse<String> breezLnUrlPayRecover(@RequestBody RegisterLnUrlWebhookReq request) {
        log.info("breezLnUrlPayRecover: {}", request);
        return breezService.breezLnurlPayRecover(request);
    }

    @Operation(summary = "LNURL支付信息接口")
    @PostMapping(path = "/breez/ln-url-pay/identifier")
    public BaseResponse<LnurlPayIdentifierResp> breezLnUrlPayIdentifier(@RequestBody LnurlPayIdentifierReq request) {
        log.info("breezLnUrlPayRecover: {}", request);
        return breezService.breezLnurlPayIdentifier(request);
    }

    @Operation(summary = "LNURL 付款发票接口")
    @PostMapping(path = "/breez/ln-url-pay/invoice")
    public BaseResponse<String> breezLnUrlPayInvoice(@RequestBody LnurlPayInvoiceReq request) {
        log.info("breezLnUrlPayInvoice: {}", request);
        return breezService.breezLnurlPayInvoice(request);
    }



    /**
     * Breez回调接口 - LNURL支付回调
     */
    @Operation(summary = "Breez 回调接口（LNURL支付）")
    @PostMapping(path = "/breez/ln-url-pay/response")
    public BaseResponse<String> breezLnUrlPayResponse(@RequestBody WebhookCallbackReq request) {
        log.info("breezLnUrlPayResponse: 接收Breez LNURL支付回调请求，参数={}", request);
        return breezService.breezLnUrlPayResponse(request);
    }



}
