package org.hcm.lifpay.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoltCarPrivacy {

    //隐私模式：0=禁用，1=启用

    FORBIDDEN(0, "forbidden", "禁用"),
    ENABLE(1, "enable", "启用"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;

}
