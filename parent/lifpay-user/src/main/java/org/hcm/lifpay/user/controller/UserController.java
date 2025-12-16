package org.hcm.lifpay.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.user.dto.req.LoginRequest;
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
    UserLoginService loginService;


    /**
     * 后台用户登录入口
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping(path = "/login")
    public @ResponseBody BaseResponse login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        log.info("");
        return loginService.login(request, httpServletResponse);
    }





}
