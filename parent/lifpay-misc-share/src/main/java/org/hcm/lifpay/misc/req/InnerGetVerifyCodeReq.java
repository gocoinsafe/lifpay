package org.hcm.lifpay.misc.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InnerGetVerifyCodeReq {


    @ApiModelProperty(value = "email/phone")
    String contact;

    @ApiModelProperty(value = "消息类型：0-邮箱，1-手机号")
    Integer type;


    @ApiModelProperty(value = "验证码")
    String verifyCode;

}
