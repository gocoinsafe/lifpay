package org.hcm.lifpay.data.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LnurlPayIdentifierResp {


    @Schema(description = "回调地址")
    String callback;

    @Schema(description = "可发送的最大金额")
    String maxSendable;

    @Schema(description = "可发送的最小金额")
    String minSendable;

    @Schema(description = "元数据")
    String metadata;

    @Schema(description = "payRequest")
    String tag;

}
