package org.hcm.lifpay.handler;


import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.DigitalResultEnum;
import org.hcm.lifpay.exception.NoAccessUserTypeException;
import org.hcm.lifpay.exception.NotLoginException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.NoPermissionException;

/**
 * @author xinzhe
 */
@RestControllerAdvice
@Slf4j
public class GlobalAuthExceptionHandler {

    /**
     * 权限码异常
     */
    @ExceptionHandler(NoPermissionException.class)
    public BaseResponse notPermissionExceptionHandle(NoPermissionException e) {
        log.error("用户没有访问的权限");
        return BaseResponse.fail(DigitalResultEnum.FORBIDDEN);
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NoAccessUserTypeException.class)
    public BaseResponse noAccessUserTypeExceptionHandle(NoAccessUserTypeException e) {
        log.error("该用户用类型没有权限访问");
        return BaseResponse.fail(DigitalResultEnum.FORBIDDEN);
    }

    /**
     * 未登录
     */
    @ExceptionHandler(NotLoginException.class)
    public BaseResponse notLoginExceptionHandle(NotLoginException e) {
        log.error("未登录");
        return BaseResponse.fail(DigitalResultEnum.NOT_LOGIN);
    }



}
