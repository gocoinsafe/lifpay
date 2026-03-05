package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.IncludePkRequest;
import org.hcm.lifpay.user.dto.req.LoginRequest;
import org.hcm.lifpay.user.dto.req.RefreshTokenReq;
import org.hcm.lifpay.user.dto.req.UpdateUserInfoReq;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.dto.resp.RefreshTokenResDto;
import org.hcm.lifpay.user.dto.resp.UserInfoResp;

import javax.servlet.http.HttpServletResponse;

public interface UserLoginService {


    /**
     * 登录验证
     *
     * @param request 请求
     * @return 返回
     */
    BaseResponse<LoginResponse> login(LoginRequest request);



    void logout(BaseRequest request);


    BaseResponse<UserInfoResp> getUserInfo(IncludePkRequest req);


    BaseResponse updateUserInfo(UpdateUserInfoReq req);


//    BaseResponse<LoginResponse> phoneLogin(PhoneLoginRequest request, HttpServletResponse httpServletResponse);

    /**
     * 刷新用户token
     *
     * @param req 请求
     * @return 返回
     */
    BaseResponse<RefreshTokenResDto> checkRefreshToken(RefreshTokenReq req);

//    BaseResponse<LoginUserInfoResponse> getLoginUserInfo(LoginUserInfoRequest request);
}
