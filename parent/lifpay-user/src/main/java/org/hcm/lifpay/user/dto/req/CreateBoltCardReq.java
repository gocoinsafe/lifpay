package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class CreateBoltCardReq extends BaseRequest {


    @ApiModelProperty(value = "NFC 芯片 UID（16位十六进制）")
    private String uid;


    @ApiModelProperty(value = "最小提现金额（sats)")
    private String minAmount;

    @ApiModelProperty(value = "最大充值金额（sats，必须 >= 1000）")
    private String maxAmount;

    @ApiModelProperty(value = "卡片名称（可选）")
    private String name;

    @ApiModelProperty(value = "PIN 码的 SHA256 值（可选）")
    private String pin;

    @ApiModelProperty(value = "隐私模式：0=禁用，1=启用（可选）")
    private String privacy;


}
