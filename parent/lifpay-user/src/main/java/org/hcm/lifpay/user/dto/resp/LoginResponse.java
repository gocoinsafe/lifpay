package org.hcm.lifpay.user.dto.resp;


import lombok.Data;

/**
 * 该类是用来响应用户登录的结果
 *
 * @author xinzhe
 */
@Data
public class LoginResponse {

    Long userId;
    String username;
    String token;
    String refreshToken;

}
