package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hcm.lifpay.common.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class IncludePkRequest extends BaseRequest {
    @ApiModelProperty(hidden = true)
    private String aesKey;
}
