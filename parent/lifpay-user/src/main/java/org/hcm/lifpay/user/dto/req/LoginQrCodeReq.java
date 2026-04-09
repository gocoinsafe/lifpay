package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class LoginQrCodeReq extends BaseRequest {

    @Schema(description = "二维码id")
    private String qrCodeId;



}
