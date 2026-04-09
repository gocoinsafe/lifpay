package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hcm.lifpay.common.BaseRequest;


@EqualsAndHashCode(callSuper = true)
@Data
public class CreateLoginQrCodeReq extends BaseRequest {

    @Schema(description = "平台类型（1：Dashboard；2：其他）")
    private int platform;

    @Schema(description = "公钥")
    private String publicKey;


}
