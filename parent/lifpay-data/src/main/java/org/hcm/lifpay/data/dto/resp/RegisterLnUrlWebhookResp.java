package org.hcm.lifpay.data.dto.resp;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterLnUrlWebhookResp {


    @Schema(description = "LNURL支付编码端点")
    @JsonProperty("lnurl")
    String lnUrl;

    @Schema(description = "lightning网络地址")
    @JsonProperty("lightning_address")
    String lightningAddress;

    @Schema(description = "username@app.domain")
    @JsonProperty("bip353_address")
    String bip353Address;

}
