package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.LoginRequest;
import org.hcm.lifpay.user.dto.resp.LoginResponse;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {


    /**
     * 登录验证
     *
     * @param request 请求
     * @return 返回
     */
    BaseResponse<LoginResponse> login(LoginRequest request, HttpServletResponse httpServletResponse);

//    AdminBaseResponse<LoginResponse> phoneLogin(PhoneLoginRequest request, HttpServletResponse httpServletResponse);

    /**
     * 登出接口
     *
     * @param request 请求
     * @return 返回
     */
//    AdminBaseResponse<LogoutResponse> logout(AdminBaseRequest request, HttpServletResponse httpServletResponse);

    /**
     * 刷新用户token
     *
     * @param req 请求
     * @return 返回
     */
//    AdminBaseResponse<RefreshTokenResDto> checkRefreshToken(RefreshTokenReq req, HttpServletResponse httpServletResponse);

//    AdminBaseResponse<LoginUserInfoResponse> getLoginUserInfo(LoginUserInfoRequest request);
}
