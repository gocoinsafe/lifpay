package org.hcm.lifpay.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {



    NORMAL(0, "normal", "正常"),
    FREEZE(1, "freeze", "冻结"),
    DELETED(2, "Deleted", "注销"),
    NONACTIVATED(3, "nonactivated", "未激活"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;

}
