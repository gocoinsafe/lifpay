package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class BoltCardsInfoReq extends BaseRequest {

    @Schema(description = "reqId 从发卡接口返回的 lnurlw_base 中提取")
    private String reqId;




}
