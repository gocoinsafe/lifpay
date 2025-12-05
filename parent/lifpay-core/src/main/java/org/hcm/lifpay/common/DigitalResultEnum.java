package org.hcm.lifpay.common;

/**
 * @Author: jerry
 * @Description 数字结果枚举
 */
public enum DigitalResultEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    
    /**
     * 失败
     */
    FAIL(500, "失败"),
    
    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    NOT_LOGIN(3, "未登录"),

    FORBIDDEN(1, "没有访问权限");

    private final int code;
    private final String desc;

    DigitalResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

