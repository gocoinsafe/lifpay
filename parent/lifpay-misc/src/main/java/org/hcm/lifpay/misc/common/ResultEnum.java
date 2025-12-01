package org.hcm.lifpay.misc.common;




public enum ResultEnum {


    //系统异常状态码
    /**
     * 系统异常，请联系相关人员处理
     */
    SYSTEM_FAIL(-1, "System exception, please contact relevant personnel for handling.","系统异常，请联系相关人员处理"),
    //成功状态码
    /**
     * 成功
     */
    SUCCESS(0, "Success","成功"),
    /**
     * 参数错误
     */
    PARAM_ERROR(2, "Parameter error","参数错误"),
    /**
     * 系统繁忙，请稍后再试
     */
    SYSTEM_BUSY(3, "System is busy, please try again later","系统繁忙，请稍后再试"),

    THE_CHARACTER_LENGTH_ERROR(100101, "The character count exceeds the length limit.","字符超过长度限制！"),
    ILLEGAL_CHARACTERS_ERROR(100102, "Illegal characters","非法字符"),
    CONTACT_INFORMATION_ERROR(100103, "Contact information cannot be empty.","联系方式不能为空"),
    NAME_NOT_NULL_ERROR(100104, "Name cannot be empty.","姓名不能为空"),
    COUNTRY_NOT_NULL_ERROR(100105, "country cannot be empty.","国家不能为空"),
    AREA_NOT_NULL_ERROR(100106, "area cannot be empty.","区号不能为空"),
    EMAIL_INFORMATION_ERROR(100107, "Email cannot be empty.","Email方式不能为空"),
    MESSAGE_NOT_NULL_ERROR(100108, "message cannot be empty.","message不能为空");




    private final Integer code;
    private final String desc;
    private String chMsg;

    ResultEnum(Integer code, String desc, String chMsg) {
        this.code = code;
        this.desc = desc;
        this.chMsg = chMsg;
    }

    public static ResultEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }
        for (ResultEnum temp : ResultEnum.values()) {
            if (code.intValue() == temp.getCode().intValue()) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 获取枚举值
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取枚举描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 获取中文描述
     */
    public String getChMsg() {
        return chMsg;
    }
}
