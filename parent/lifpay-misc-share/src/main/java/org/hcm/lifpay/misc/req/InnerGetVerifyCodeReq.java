package org.hcm.lifpay.misc.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InnerGetVerifyCodeReq {


    @Schema(description = "email/phone")
    String contact;

    @Schema(description = "消息类型：0-邮箱，1-手机号")
    Integer type;


    @Schema(description = "验证码")
    String verifyCode;

}
