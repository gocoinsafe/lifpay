package org.hcm.lifpay.user.exception;

import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.exception.CommonRuntimeException;
import org.hcm.lifpay.user.dto.UserResultEnum;

public class ErrorCodeException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    private UserResultEnum userResultEnum;
    private String msg;

    public ErrorCodeException(UserResultEnum resultEnum) {
        this.userResultEnum = resultEnum;
    }

    public ErrorCodeException(UserResultEnum resultEnum, String msg) {
        this.msg = msg;
        this.userResultEnum = resultEnum;
    }

    public UserResultEnum getUserResultEnum() {
        return userResultEnum;
    }


    @Override
    public String getMessage() {
        if (StringUtils.isEmpty(msg)) {
            return userResultEnum.getMsg();
        }
        return msg;
    }
}
