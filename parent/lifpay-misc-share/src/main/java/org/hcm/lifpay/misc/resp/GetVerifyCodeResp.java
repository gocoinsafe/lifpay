package org.hcm.lifpay.misc.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetVerifyCodeResp {

    @ApiModelProperty(value = "是否成功")
    private Boolean result;
}
