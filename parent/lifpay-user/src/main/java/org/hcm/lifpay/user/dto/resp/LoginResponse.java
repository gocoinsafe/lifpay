package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 该类是用来响应用户登录的结果
 *
 * @author xinzhe
 */
@Data
public class LoginResponse {

    @ApiModelProperty(value = "用户id")
    Long userId;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty(value = "token")
    String token;

    @ApiModelProperty(value = "刷新token")
    String refreshToken;

}
