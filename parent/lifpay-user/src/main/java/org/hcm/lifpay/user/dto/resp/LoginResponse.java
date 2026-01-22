package org.hcm.lifpay.user.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 该类是用来响应用户登录的结果
 *
 * @author xinzhe
 */
@Data
public class LoginResponse {

    @Schema(description = "用户id")
    Long userId;

    @Schema(description = "用户名")
    String username;

    @Schema(description = "token")
    String token;

    @Schema(description = "刷新token")
    String refreshToken;

}
