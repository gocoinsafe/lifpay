package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

/**
 * @program: 该类是管理后台刷新token 的请求类
 * @description:
 * @author:
 **/
@Data
public class RefreshTokenReq extends BaseRequest {

    @ApiModelProperty("登录返回的refreshToken")
    private String refreshToken;

    @ApiModelProperty("privateContent")
    private String privateContent;

    @ApiModelProperty("aesKey")
    private String aesKey;

}
