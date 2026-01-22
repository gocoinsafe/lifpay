package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class CreateBoltCardReq extends BaseRequest {


    @Schema(description = "NFC 芯片 UID（16位十六进制）")
    private String uid;


    @Schema(description = "最小提现金额（sats)")
    private String minAmount;

    @Schema(description = "最大充值金额（sats，必须 >= 1000）")
    private String maxAmount;

    @Schema(description = "卡片名称（可选）")
    private String name;

    @Schema(description = "PIN 码的 SHA256 值（可选）")
    private String pin;

    @Schema(description = "隐私模式：0=禁用，1=启用（可选）")
    private String privacy;


}
