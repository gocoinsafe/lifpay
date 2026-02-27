package org.hcm.lifpay.data.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.data.common.DataResultEnum;
import org.hcm.lifpay.data.dto.req.LnurlPayIdentifierReq;
import org.hcm.lifpay.data.dto.req.LnurlPayInvoiceReq;
import org.hcm.lifpay.data.dto.req.RegisterLnUrlWebhookReq;
import org.hcm.lifpay.data.dto.req.WebhookCallbackReq;
import org.hcm.lifpay.data.dto.resp.LnurlPayIdentifierResp;
import org.hcm.lifpay.data.dto.resp.RegisterLnUrlWebhookResp;
import org.hcm.lifpay.data.service.BreezService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@RefreshScope
@Service
public class BreezServiceImpl implements BreezService {


    private final static Logger logger = LoggerFactory.getLogger(BreezServiceImpl.class);


    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final String BREEZ_NODE_API_URL = "https://test.lifpay.me/lnurlpay/";


    @Override
    public BaseResponse<RegisterLnUrlWebhookResp> breezRegisterLnUrlWebHook(RegisterLnUrlWebhookReq request) {
        logger.info("breezRegisterLnUrlWebHook request: {}", request);
        BaseResponse<RegisterLnUrlWebhookResp> response = new BaseResponse<>();

        String url = BREEZ_NODE_API_URL + request.getPubkey();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("time", request.getTime());
        bodyMap.put("webhook_url", request.getWebhookUrl());
        bodyMap.put("signature", request.getSignature());

        if (request.getUserName() != null) {
            bodyMap.put("username", request.getUserName());
        }
        if (request.getOffer() != null) {
            bodyMap.put("offer", request.getOffer());
        }

        try {
            String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);
            logger.info("breezRegisterLnUrlWebHook.Breez.request: {}", jsonBody);
            String resp = postJson(url, jsonBody);
            logger.info("breezRegisterLnUrlWebHook.Breez.response: {}", resp);
            ObjectMapper objectMapper = new ObjectMapper();
            if (StringUtils.isNotEmpty(resp)){
                RegisterLnUrlWebhookResp lnurlResp = objectMapper.readValue(resp, RegisterLnUrlWebhookResp.class);

                response.setData(lnurlResp);
                logger.info("breezRegisterLnUrlWebHook.Breez.response: {}", resp);
            }else {
                response.setCode(DataResultEnum.REGISTER_BREEZ_API_ERROR.getCode());
                response.setMessage(DataResultEnum.REGISTER_BREEZ_API_ERROR.getDesc());
                return response;
            }
           
        } catch (Exception e) {
            logger.error("Register LNURL webhook failed", e);
            response.setCode(DataResultEnum.REGISTER_BREEZ_API_ERROR.getCode());
            response.setMessage(DataResultEnum.REGISTER_BREEZ_API_ERROR.getDesc());
            return response;
        }
        return response;
    }


    @Override
    public BaseResponse<String> breezUnregisterLnUrlWebHook(RegisterLnUrlWebhookReq request) {
        logger.info("breezUnregisterLnUrlWebHook request: {}", request);
        BaseResponse<String> response = new BaseResponse<>();

        // Endpoint: DELETE /lnurlpay/{pubkey}
        String url = BREEZ_NODE_API_URL + request.getPubkey();

        // 构造请求体
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("time", request.getTime());
        bodyMap.put("webhook_url", request.getWebhookUrl());
        bodyMap.put("signature", request.getSignature()); // signature of "-<webhook_url>"

        try {
            String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);
            logger.info("breezUnregisterLnUrlWebHook.Breez.request: {}", jsonBody);
            RequestBody body = RequestBody.create(JSON, jsonBody);

            Request httpRequest = new Request.Builder()
                    .url(url)
                    .delete(body) // DELETE 方法带 JSON body
                    .build();

            try (Response resp = CLIENT.newCall(httpRequest).execute()) {
                if (!resp.isSuccessful()) {
                    response.setCode(DataResultEnum.UNREGISTER_BREEZ_API_ERROR.getCode());
                    response.setMessage(DataResultEnum.UNREGISTER_BREEZ_API_ERROR.getDesc());
                    return response;
//                    throw new RuntimeException("HTTP " + resp.code() + " - " + resp.message());
                }

                String respBody = resp.body() != null ? resp.body().string() : "";
                logger.info("breezUnregisterLnUrlWebHook response: {}", respBody);
                response.setData(respBody);
            }

        } catch (Exception e) {
            logger.error("Unregister LNURL webhook failed", e);
        }

        return response;
    }

    @Override
    public BaseResponse<String> breezLnurlPayRecover(RegisterLnUrlWebhookReq request) {
        logger.info("breezLnurlPayRecover request: {}", request);
        BaseResponse<String> response = new BaseResponse<>();

        // Endpoint: POST /lnurlpay/{pubkey}/recover
        String url = BREEZ_NODE_API_URL + request.getPubkey() + "/recover";

        // 构造请求体
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("time", request.getTime());
        bodyMap.put("webhook_url", request.getWebhookUrl());
        bodyMap.put("signature", request.getSignature()); // signature of "-<webhook_url>"

        try {
            // 将 Map 转为 JSON 字符串
            String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);
            logger.info("breezLnurlPayRecover.Breez.request: {}", jsonBody);
            RequestBody body = RequestBody.create(JSON, jsonBody);

            Request httpRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response resp = CLIENT.newCall(httpRequest).execute()) {
                if (!resp.isSuccessful()) {
                    throw new RuntimeException("HTTP " + resp.code() + " - " + resp.message());
                }

                String respBody = resp.body() != null ? resp.body().string() : "";
                logger.info("breezLnurlPayRecover response: {}", respBody);
                response.setData(respBody);
            }

        } catch (Exception e) {
            logger.error("LNURL Pay recover failed", e);
        }

        return response;
    }


    @Override
    public BaseResponse<LnurlPayIdentifierResp> breezLnurlPayIdentifier(LnurlPayIdentifierReq request) {
        logger.info("breezLnurlPayIdentifier request: {}", request);
        BaseResponse<LnurlPayIdentifierResp> response = new BaseResponse<>();

        // Endpoint: GET /lnurlp/{identifier}
        String url = BREEZ_NODE_API_URL + request.getIdentifier();

        try {
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response resp = CLIENT.newCall(httpRequest).execute()) {
                if (!resp.isSuccessful()) {
                    throw new RuntimeException("HTTP " + resp.code() + " - " + resp.message());
                }

                String respBody = resp.body() != null ? resp.body().string() : "";
                logger.info("breezLnurlPayIdentifier response: {}", respBody);

                if (!respBody.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    LnurlPayIdentifierResp lnurlResp = objectMapper.readValue(respBody, LnurlPayIdentifierResp.class);
                    response.setData(lnurlResp);
                }
            }

        } catch (Exception e) {
            logger.error("LNURL Pay Identifier request failed", e);
        }

        return response;
    }


    @Override
    public BaseResponse<String> breezLnurlPayInvoice(LnurlPayInvoiceReq request) {
    logger.info("breezLnurlPayInvoice request: {}",request);
        BaseResponse<String> response = new BaseResponse<>();

        try {
            // 构造 URL 并添加 query 参数
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BREEZ_NODE_API_URL + request.getIdentifier() + "/invoice")
                    .newBuilder()
                    .addQueryParameter("amount", String.valueOf(request.getAmount()));

            if (request.getComment() != null && !request.getComment().isEmpty()) {
                urlBuilder.addQueryParameter("comment", request.getComment());
            }

            String url = urlBuilder.build().toString();
            logger.info("LNURL Pay Invoice URL: {}", url);

            // 构造 GET 请求
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response resp = CLIENT.newCall(httpRequest).execute()) {
                if (!resp.isSuccessful()) {
                    throw new RuntimeException("HTTP " + resp.code() + " - " + resp.message());
                }

                String respBody = resp.body() != null ? resp.body().string() : "";
                logger.info("breezLnurlPayInvoice response: {}", respBody);

                response.setData(respBody);
            }

        } catch (Exception e) {
            logger.error("LNURL Pay Invoice request failed", e);
        }

        return response;
    }


    @Override
    public BaseResponse<String> breezLnUrlPayResponse(WebhookCallbackReq request) {
        logger.info("breezLnUrlPayResponse request: {}", request);





        return new BaseResponse<>();
    }

    public String postJson(String url, String jsonBody) throws IOException {

        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {

                logger.error("HTTP " + response);
                throw new RuntimeException("HTTP " + response.code());
            }
            return response.body().string();
        }
    }

}
