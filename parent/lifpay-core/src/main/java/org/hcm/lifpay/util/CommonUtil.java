package org.hcm.lifpay.util;

import java.security.SecureRandom;
import java.text.DecimalFormat;

public class CommonUtil {


    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    // 6位数字格式化器（不足6位时前面补零）
    private static final DecimalFormat SIX_DIGIT_FORMAT = new DecimalFormat("000000");

    /**
     * 生成6位数字验证码（通用版，支持补零）
     * @return 6位数字字符串（如 008976）
     */
    public static String generate6DigitCode() {
        // 生成 [0, 999999] 之间的随机整数
        int randomNum = SECURE_RANDOM.nextInt(1000000);
        // 格式化补零，确保6位
        return SIX_DIGIT_FORMAT.format(randomNum);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("6位验证码：" + generate6DigitCode());
        }
    }


}
