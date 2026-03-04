package org.hcm.lifpay.data.common;

public enum DataResultEnum {
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

    REGISTER_BREEZ_API_ERROR(20001, "register Breez Interface Error","注册Breez接口报错"),
    UNREGISTER_BREEZ_API_ERROR(20002, "Unregister Breez Interface Error","取消注册Breez接口报错"),

    TRANSACTION_INSERTION_FAILED_ERROR(20003, "Transaction record insertion failed","交易记录插入失败"),

    ;


    private final Integer code;
    private final String desc;
    private String chMsg;

    DataResultEnum(Integer code, String desc, String chMsg) {
        this.code = code;
        this.desc = desc;
        this.chMsg = chMsg;
    }

    public static DataResultEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }
        for (DataResultEnum temp : DataResultEnum.values()) {
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
