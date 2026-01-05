package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class BoltCardsInfoReq extends BaseRequest {

    @ApiModelProperty(value = "reqId 从发卡接口返回的 lnurlw_base 中提取")
    private String reqId;




}
