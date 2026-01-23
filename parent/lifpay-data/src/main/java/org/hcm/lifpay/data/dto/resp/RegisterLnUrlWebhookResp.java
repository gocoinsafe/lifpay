package org.hcm.lifpay.data.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterLnUrlWebhookResp {


    @Schema(description = "LNURL支付编码端点")
    String lnUrl;

    @Schema(description = "lightning网络地址")
    String lightningAddress;

    @Schema(description = "username@app.domain")
    String bip353Address;

}
