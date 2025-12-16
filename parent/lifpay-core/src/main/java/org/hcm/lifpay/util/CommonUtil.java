package org.hcm.lifpay.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

public class CommonUtil {



    /**
     * 生成6位数字验证码（通用版，支持补零）
     * @return 6位数字字符串（如 008976）
     */
    public static String getRandomInteger(int len) {
        SecureRandom random = new SecureRandom();
        BigDecimal randomBd = BigDecimal.valueOf(random.nextDouble());
        BigInteger randomNum = randomBd.multiply(new BigDecimal(10).pow(len + 3)).toBigInteger();
        String randomStr = String.valueOf(randomNum);
        if(randomStr.length() > len){
            return randomStr.substring(0, len);
        }
        if(randomStr.length() < len){
            return StringUtils.leftPad(randomStr, len, "0");
        }
        return randomStr;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("6位验证码：" + getRandomInteger(6));
        }
    }


}
