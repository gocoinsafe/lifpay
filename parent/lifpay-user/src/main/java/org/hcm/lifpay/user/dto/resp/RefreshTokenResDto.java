package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RefreshTokenResDto {

    /**
     * 服务返回新的token 给到前端
     */
    @ApiModelProperty("服务返回新的token 给到前端")
    String token;
    /**
     * 服务返回新的Refresh token 给到前端
     */
    @ApiModelProperty("服务返回新的Refresh token 给到前端")
    String refreshToken;


    String publicKey;
}
