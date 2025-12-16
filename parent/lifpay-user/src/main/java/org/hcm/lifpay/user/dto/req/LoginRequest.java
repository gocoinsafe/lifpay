package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hcm.lifpay.common.BaseRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginRequest extends BaseRequest {

    @ApiModelProperty(value = "email/phone")
    String contact;

    @ApiModelProperty(value = "消息类型：0-邮箱，1-手机号")
    Integer type;

    @ApiModelProperty(value = "验证码")
    String verifyCode;

    @ApiModelProperty(value = "密码")
    String password;

    String aesKey;

    String privateContent;
}
