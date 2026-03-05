package org.hcm.lifpay.user.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.*;
import org.hcm.lifpay.user.dto.resp.AgreementListDto;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.dto.resp.RefreshTokenResDto;
import org.hcm.lifpay.user.dto.resp.UserInfoResp;
import org.hcm.lifpay.user.service.AccountService;
import org.hcm.lifpay.user.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(path = "/api/user", consumes = "application/json")
@Slf4j
public class UserController {


    @Autowired
    UserLoginService userLoginService;

    @Autowired
    AccountService accountService;


    /**
     * 用户登录入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/login")
    public @ResponseBody BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("UserController.login:{}", JSON.toJSONString(request));
        return userLoginService.login(request);
    }


    @PostMapping(path = "/userinfo")
    @ApiOperation(value = "获取用户信息")
    public @ResponseBody
    BaseResponse<UserInfoResp> getUserInfo(@RequestBody IncludePkRequest req) {
        log.info("getUserInfo request {}", JSON.toJSONString(req));
        BaseResponse<UserInfoResp> response = userLoginService.getUserInfo(req);
        log.info("getUserInfo response {}", JSON.toJSONString(response));
        return response;
    }


    @PostMapping(path = "/refresh_token")
    @ApiImplicitParam(value = "token过期 刷新token")
    public @ResponseBody BaseResponse<RefreshTokenResDto> refreshToken(@RequestBody RefreshTokenReq request) {
        log.info("refreshToken request: " + JSON.toJSONString(request));
        BaseResponse<RefreshTokenResDto> response = userLoginService.checkRefreshToken(request);
        log.info("refreshToken response: " + JSON.toJSONString(response));
        return response;
    }




    /**
     * 用户退出入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/logout")
    public BaseResponse logout(@RequestBody BaseRequest request) {
        log.info("UserController.logout:{}", JSON.toJSONString(request));
        userLoginService.logout(request);
        log.info("finish logout " + JSON.toJSONString(request));
        return new BaseResponse();
    }


    @PostMapping(path = "/agreement/list")
    @ApiOperation(value = "协议列表")
    public BaseResponse<AgreementListDto> agreementList(@Valid @RequestBody AgreementQueryReq req){
        log.info("agreementList req:{}", JSON.toJSONString(req));
        BaseResponse<AgreementListDto> response = accountService.agreementList(req);
        log.info("agreementList response: {}", JSON.toJSONString(response));
        return response;
    }

    @PostMapping(path = "/agreement/query")
    @ApiOperation(value = "协议查询")
    public BaseResponse<AgreementListDto> agreementQuery(@Valid @RequestBody AgreementQueryReq req){
        log.info("agreementQuery req:{}", JSON.toJSONString(req));
        BaseResponse<AgreementListDto> response = accountService.agreementQuery(req);
        log.info("agreementQuery response: {}", JSON.toJSONString(response));
        return response;
    }

    @PostMapping(path = "/agreement/agree")
    @ApiOperation(value = "同意协议")
    public BaseResponse agreementAgree(@Valid @RequestBody AgreementAgreeReq req){
        log.info("agreementAgree req:{} ", JSON.toJSONString(req));
        BaseResponse response = accountService.agreementAgree(req);
        log.info("agreementAgree response "+ JSON.toJSONString(response));
        return response;
    }


    @PostMapping(path = "/upload/image")
    @ApiOperation(value = "上传图像")
    public BaseResponse uploadImage(@Valid @RequestBody AgreementAgreeReq req){
        log.info("uploadImage req:{} ", JSON.toJSONString(req));
        BaseResponse response = accountService.agreementAgree(req);
        log.info("uploadImage response "+ JSON.toJSONString(response));
        return response;
    }


    @PostMapping(path = "/update/userInfo")
    @ApiOperation(value = "修改用户信息")
    public BaseResponse updateUserInfo(@Valid @RequestBody UpdateUserInfoReq req){
        log.info("updateUserInfo req:{} ", JSON.toJSONString(req));
        BaseResponse response = userLoginService.updateUserInfo(req);
        log.info("updateUserInfo response "+ JSON.toJSONString(response));
        return response;
    }


    @PostMapping(path = "/update/lightning/address")
    @ApiOperation(value = "上传用户Lightning地址")
    public BaseResponse updateLightningAddress(@Valid @RequestBody UserLightningAddressReq req){
        log.info("updateLightningAddress req:{} ", JSON.toJSONString(req));
        BaseResponse response = userLoginService.updateLightningAddress(req);
        log.info("updateLightningAddress response "+ JSON.toJSONString(response));
        return response;
    }



}
