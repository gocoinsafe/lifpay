package org.hcm.lifpay.misc.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class GetVerifyCodeReq extends BaseRequest {

    @ApiModelProperty(value = "联系方式：邮箱/手机号")
    String contact;

    @ApiModelProperty(value = "消息类型：0-邮箱，1-手机号")
    Integer type;



}
