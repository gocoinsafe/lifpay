package org.hcm.lifpay.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoltCarStatus {


    NORMAL(0, "normal", "使用中"),
    DELETED(1, "Deleted", "删除"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;

}
