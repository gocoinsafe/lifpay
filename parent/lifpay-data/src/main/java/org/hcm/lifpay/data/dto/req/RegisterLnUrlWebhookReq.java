package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
@Schema(description = "Breez LnurlPay 请求参数")
public class RegisterLnUrlWebhookReq extends BaseRequest {

    @Schema(description = "pubkey", required = true)
    String pubkey;


    @Schema(description = "时间戳", required = true)
    Long time;

    @Schema(description = "webhook url")
    String webhookUrl;

    @Schema(description = "用户名")
    String userName;

    @Schema(description = "报价")
    String offer;

    @Schema(description = "签名信息")
    String signature;



}
