package org.hcm.lifpay.misc.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerifyCodeTypeEnum {


    EMAIL(0, "Email", "邮箱"),
    PHONE(1, "phone", "手机号"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;

}
