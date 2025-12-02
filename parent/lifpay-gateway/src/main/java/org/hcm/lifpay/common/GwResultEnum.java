package org.hcm.lifpay.common;

public enum GwResultEnum {

    NO_LOGIN(401, "未登录"),
    NO_IN_WHITELIST(2001, "抱歉，您不在内测范围内"),
    REQUEST_ERROR(2002, "错误的请求"),
    FORCE_LOGOUT(2003, "新设备登录，强制登出"),
    SERVER_DISABLE(2004, "抱歉，服务器临时开小差，马上回来！"),
    NO_AUTH(2005, "无权限"),
    SIGN_ERROR(2006, "签名错误"),
    PARAM_ERROR(2007, "参数错误");

    private int code;
    private String msg;

    private GwResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
