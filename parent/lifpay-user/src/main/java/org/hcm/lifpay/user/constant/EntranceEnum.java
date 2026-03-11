package org.hcm.lifpay.user.constant;



/**
 * 该枚举类是为了区分 是DashBoard还是 其他场景
 *
 * @author xinzhe
 */
public enum EntranceEnum {


    //DashBoard
    DASHBOARD(1),
    //其他
    OTHER(2);

    private Integer value;

    EntranceEnum(Integer value) {
        this.value = value;
    }

    public static EntranceEnum fromValue(int value) {
        for (EntranceEnum b : EntranceEnum.values()) {
            if (b.getValue() == (value)) {
                return b;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}
