package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
@Schema(description = "Breez LnurlPay 请求参数")
public class RegisterLnUrlWebhookReq extends BaseRequest {

    @Schema(description = "pubkey", required = true)
    String pubkey;

}
