package org.hcm.lifpay.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeMethodEnum {

    LIGHTNING(1, "lightning", "闪电支付"),
    OTHER(2, "other", "其他"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;


    /**
     * 根据type值获取对应的枚举实例
     * @param type 枚举的type值（支付方式：1=lightning，2=其他（必填）
     * @return 匹配的枚举实例，无匹配则返回null
     */
    public static TradeMethodEnum getByType(Integer type) {
        // 1. 入参null直接返回null，避免空指针
        if (type == null) {
            return null;
        }
        // 2. 遍历所有枚举常量，匹配type值
        for (TradeMethodEnum enumItem : TradeMethodEnum.values()) {
            if (enumItem.getType().equals(type)) {
                return enumItem;
            }
        }
        // 3. 无匹配值返回null
        return null;
    }


}
