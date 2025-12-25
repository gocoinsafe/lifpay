package org.hcm.lifpay.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {


    PERSON(0, "person", "个人"),
    ENTERPRISE(1, "enterprise", "企业"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;
}
