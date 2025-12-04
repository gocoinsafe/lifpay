package org.hcm.lifpay.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.LoginRequest;
import org.hcm.lifpay.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

//import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(path = "/api/user", consumes = "application/json")
@Slf4j
public class LoginController {


    @Autowired
    LoginService loginService;


    /**
     * 后台用户登录入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/login")
    public @ResponseBody BaseResponse login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {

        return loginService.login(request, httpServletResponse);
    }





}
