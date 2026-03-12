package org.hcm.lifpay.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStatusEnum {

    COMPLETED(1, "completed", "完成"),
    PENDING(2, "pending", "处理中"),
    FAILED(3, "failed", "失败"),
    ;

    private final Integer type;

    private final String name;

    private final String desc;


    /**
     * 根据type值获取对应的枚举实例
     * @param type 枚举的type值 支付状态：1=完成，2=处理中，3=失败
     * @return 匹配的枚举实例，无匹配则返回null
     */
    public static TradeStatusEnum getByType(Integer type) {
        // 1. 入参null直接返回null，避免空指针
        if (type == null) {
            return null;
        }
        // 2. 遍历所有枚举常量，匹配type值
        for (TradeStatusEnum enumItem : TradeStatusEnum.values()) {
            if (enumItem.getType().equals(type)) {
                return enumItem;
            }
        }
        // 3. 无匹配值返回null
        return null;
    }

    public static TradeStatusEnum getFromName(String name) {
        for (TradeStatusEnum b : TradeStatusEnum.values()) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

}
