package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hcm.lifpay.common.BaseRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginRequest extends BaseRequest {

    @Schema(description = "email/phone")
    String contact;

    @Schema(description = "消息类型：0-邮箱，1-手机号")
    Integer type;

    @Schema(description = "验证码")
    String verifyCode;

    @Schema(description = "密码")
    String password;

    String aesKey;

    String privateContent;
}
