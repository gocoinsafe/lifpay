package org.hcm.lifpay.user.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ScanQrCodeResp {

    @Schema(description = "平台类型（1：Dashboard；2：其他）")
    private int platform;
}
