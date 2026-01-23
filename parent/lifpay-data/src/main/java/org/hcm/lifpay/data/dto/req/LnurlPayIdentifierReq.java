package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
@Schema(description = "Breez LnurlPay 请求参数")
public class LnurlPayIdentifierReq extends BaseRequest {

    @Schema(description = "identifier", required = true)
    String identifier;

}
