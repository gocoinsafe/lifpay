package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hcm.lifpay.common.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class IncludePkRequest extends BaseRequest {
    @Schema(hidden = true)
    private String aesKey;
}
