package org.hcm.lifpay.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeTypeEnum {

    SEND(1, "send", "转出"),
    RECEIVE(2, "receive", "转入"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;


    /**
     * 根据type值获取对应的枚举实例
     * @param type 枚举的type值（交易类型：1=发送，2=接收
     * @return 匹配的枚举实例，无匹配则返回null
     */
    public static TradeTypeEnum getByType(Integer type) {
        // 1. 入参null直接返回null，避免空指针
        if (type == null) {
            return null;
        }
        // 2. 遍历所有枚举常量，匹配type值
        for (TradeTypeEnum enumItem : TradeTypeEnum.values()) {
            if (enumItem.getType().equals(type)) {
                return enumItem;
            }
        }
        // 3. 无匹配值返回null
        return null;
    }
}
