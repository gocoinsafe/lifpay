package org.hcm.lifpay.misc.common;


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


    /**
     * 根据type值获取对应的枚举实例
     * @param type 枚举的type值（0=邮箱，1=手机号）
     * @return 匹配的枚举实例，无匹配则返回null
     */
    public static VerifyCodeTypeEnum getByType(Integer type) {
        // 1. 入参null直接返回null，避免空指针
        if (type == null) {
            return null;
        }
        // 2. 遍历所有枚举常量，匹配type值
        for (VerifyCodeTypeEnum enumItem : VerifyCodeTypeEnum.values()) {
            if (enumItem.getType().equals(type)) {
                return enumItem;
            }
        }
        // 3. 无匹配值返回null
        return null;
    }
}
