package org.hcm.lifpay.user.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CreateLoginQrCodeResp {

    @Schema(description = "二维码id")
    private String qrCodeId;


    @Schema(description = "超时时间")
    private long expireTime;
}
