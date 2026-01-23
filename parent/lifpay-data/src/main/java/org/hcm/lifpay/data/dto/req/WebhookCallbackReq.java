package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class WebhookCallbackReq extends BaseRequest {

    @Schema(description = "responseID", required = true)
    String responseID;

}
