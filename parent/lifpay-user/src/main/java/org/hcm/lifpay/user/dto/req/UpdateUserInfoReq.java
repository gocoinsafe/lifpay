package org.hcm.lifpay.user.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

@Data
public class UpdateUserInfoReq extends BaseRequest {


    @Schema(description = "头像图片地址")
    String iconUrl;

    @Schema(description = "昵称")
    String nickName;

    @Schema(description = "个人简介")
    String bio;

}
