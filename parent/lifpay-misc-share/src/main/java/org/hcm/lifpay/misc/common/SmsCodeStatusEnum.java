package org.hcm.lifpay.misc.common;


/**
 * 短信验证码状态
 * @author xinzhe
 */
public enum SmsCodeStatusEnum {

    NOT_USED(0, "未使用"),
    USED(1, "已使用"),
    EXPIRE(2, "已过期"),
    ;

    private final Integer code;
    private final String desc;

    SmsCodeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SmsCodeStatusEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }
        for (SmsCodeStatusEnum temp : SmsCodeStatusEnum.values()) {
            if (code.intValue() == temp.getCode().intValue()) {
                return temp;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
