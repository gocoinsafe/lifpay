package org.hcm.lifpay.misc.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetVerifyCodeResp {

    @Schema(description = "是否成功")
    private Boolean result;
}
