package org.hcm.lifpay.user.service;

import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.*;
import org.hcm.lifpay.user.dto.resp.*;

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


    BaseResponse updateLightningAddress(UserLightningAddressReq req);
    /**
     * 刷新用户token
     *
     * @param req 请求
     * @return 返回
     */
    BaseResponse<RefreshTokenResDto> checkRefreshToken(RefreshTokenReq req);



    /**
     * 创建扫码登录二维码
     * */
    BaseResponse<CreateLoginQrCodeResp> createLoginQrCode(CreateLoginQrCodeReq req);

    /**
     * 扫登录二维码
     * */
    BaseResponse<ScanQrCodeResp> scanLoginQrCode(LoginQrCodeReq req);
    /**
     * 查询登录二维码
     * */
    BaseResponse<LoginQrCodeStateResp> getLoginQrCodeState(LoginQrCodeReq req, HttpServletResponse httpServletResponse);
    /**
     * 登录确认
     * */
    BaseResponse confirmLogin(LoginQrCodeReq req);

    /**
     * 取消登录
     * */
    BaseResponse cancelLogin(LoginQrCodeReq req);
}
