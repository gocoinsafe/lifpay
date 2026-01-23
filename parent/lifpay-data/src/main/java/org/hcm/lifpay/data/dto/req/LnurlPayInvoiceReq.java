package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class LnurlPayInvoiceReq extends BaseRequest {

    @Schema(description = "identifier", required = true)
    String identifier;

    @Schema(description = "金额", required = true)
    String amount;

    @Schema(description = "备注")
    String comment;


}
