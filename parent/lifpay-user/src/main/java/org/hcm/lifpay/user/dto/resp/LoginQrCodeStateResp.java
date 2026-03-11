package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginQrCodeStateResp {


    @Schema(description = "0：wait scan； 1:已经扫码； 2：确认登录")
    private int state;

    @Schema()
    private String refreshToken;




}
