package org.hcm.lifpay.common.enums;

/**
 * 用户身份类型
 *
 * @author xinzhe
 */
public enum AdminUserTypeEnum {
    /**
     * 超级管理员
     */
    ADMIN(0, "超级管理员"),
    /**
     * 系统管理用户
     */
    SYSTEM_USER(1, "系统管理用户"),
    /**
     * 应用管理用户
     */
    APP_USER(2, "应用管理用户"),
    /**
     * 应用管理用户
     */
    APP_SUB_USER(2, "应用子用户"),
    ;

    private int code;
    private String desc;

    AdminUserTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AdminUserTypeEnum getEnum(Integer code) {
        if (null == code) {
            return null;
        }
        for (AdminUserTypeEnum item : AdminUserTypeEnum.values()) {
            if (code == item.getCode()) {
                return item;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
