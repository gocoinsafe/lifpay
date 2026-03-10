package org.hcm.lifpay.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {


    PERSON(1, "person", "个人"),
    ENTERPRISE(2, "enterprise", "企业"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;
}
