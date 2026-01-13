package org.hcm.lifpay.user.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.IncludePkRequest;
import org.hcm.lifpay.user.dto.req.LoginRequest;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.dto.resp.UserInfoResp;
import org.hcm.lifpay.user.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(path = "/api/user", consumes = "application/json")
@Slf4j
public class UserController {


    @Autowired
    UserLoginService userLoginService;


    /**
     * 用户登录入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/login")
    public @ResponseBody BaseResponse<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        log.info("UserController.login:{}", JSON.toJSONString(request));
        return userLoginService.login(request, httpServletResponse);
    }


    @PostMapping(path = "/userinfo")
    @ApiOperation(value = "获取用户信息",tags = "迭代37")
    public @ResponseBody
    BaseResponse<UserInfoResp> getUserInfo(@RequestBody IncludePkRequest req) {
        log.info("getUserInfo request {}", JSON.toJSONString(req));
        BaseResponse<UserInfoResp> response = userLoginService.getUserInfo(req);
        log.info("getUserInfo response {}", JSON.toJSONString(response));
        return response;
    }

    /**
     * 用户退出入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/logout")
    public BaseResponse logout(@RequestBody BaseRequest request, HttpServletResponse httpServletResponse) {
        log.info("UserController.logout:{}", JSON.toJSONString(request));
        userLoginService.logout(request, httpServletResponse);
        log.info("finish logout " + JSON.toJSONString(request));
        return new BaseResponse();
    }




}
