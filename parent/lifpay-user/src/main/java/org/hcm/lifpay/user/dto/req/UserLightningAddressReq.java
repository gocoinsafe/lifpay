package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class UserLightningAddressReq extends BaseRequest {

    @Schema(description = "Lightning地址")
    String lightningAddress;

}
