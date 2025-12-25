package org.hcm.lifpay.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 正则校验工具类（含国外手机号 + 通用邮箱校验）
 */
public class RegexUtils {


    // ========== 新增：邮箱正则 ==========
    /**
     * 通用邮箱正则（适配99%以上合法邮箱，符合RFC 5322简化版）
     * 匹配示例：
     * 合法：user@domain.com、user.name+tag@domain.co.uk、user-name_123@domain.io
     * 非法：.user@domain.com、user.@domain.com、user@.com、user@domain.c
     */
    private static final String REGEX_EMAIL =
            "^[a-zA-Z0-9_+-]+(\\.[a-zA-Z0-9_+-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";

    // ========== 新增：邮箱校验方法 ==========
    /**
     * 通用邮箱格式校验
     * @param email 待校验邮箱
     * @return true=合法，false=非法/空
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        // 先过滤超长邮箱（实际业务中邮箱长度一般不超过254字符，RFC规定最大值）
        if (email.length() > 254) {
            return false;
        }
        return email.matches(REGEX_EMAIL);
    }

    // ========== 原有方法（国外手机号）保持不变 ==========
    private static final String REGEX_FOREIGN_PHONE_GENERAL =
            "^\\+?[0-9]{1,3}[\\s\\-\\.\\(\\)]?[0-9]{1,5}[\\s\\-\\.\\(\\)]?[0-9]{1,5}[\\s\\-\\.\\(\\)]?[0-9]{1,5}$";

    private static final String REGEX_US_PHONE =
            "^\\+?1[\\s\\-\\(]?\\d{3}[\\s\\-\\)]?\\d{3}[\\s\\-]?\\d{4}$";

    // 其他原有常量、方法（isForeignPhone、cleanPhoneFormat等）保持不变...

    private RegexUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
