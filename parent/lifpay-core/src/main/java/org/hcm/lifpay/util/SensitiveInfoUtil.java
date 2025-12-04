package org.hcm.lifpay.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

public class SensitiveInfoUtil {

    private static final Logger log = LoggerFactory.getLogger(SensitiveInfoUtil.class);
    private static final String SENSITIVE_IV_SEED = "qazwsxedcrfvtgby";
    @Value("${system.sensitiveCipherKey}")
    public static String sensitiveCipherKey;

    public SensitiveInfoUtil() {
    }

    public static String encrypt(String sensitiveWord, String sensitiveCipherKey) {
        if(StringUtils.isEmpty(sensitiveWord)){
            return "";
        }
        return AESCBCUtils.encrypt(sensitiveWord, getEncodeKey(sensitiveCipherKey));
    }

    public static String decrypt(String sensitiveWord, String sensitiveCipherKey) {
        if(StringUtils.isEmpty(sensitiveWord)){
            return "";
        }
        return AESCBCUtils.decrypt(sensitiveWord, getEncodeKey(sensitiveCipherKey));
    }

    public static String getEncodeKey(String password) {
        byte[] hash = HashUtil.sha256(password.getBytes());
        return HexUtil.toHexString(hash).substring(0, 16);
    }

    public static String apiEncrypt(String param, String key) {
        return AESCBCUtils.encrypt(param, getEncodeKey(key), "qazwsxedcrfvtgby");
    }

    public static String apiDecrypt(String param, String key) {
        return AESCBCUtils.decrypt(param, getEncodeKey(key), "qazwsxedcrfvtgby");
    }

    public static String handlePhone(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String decryptPhone(String cipherPhone) {
        return AESCBCUtils.decrypt(cipherPhone, getEncodeKey(sensitiveCipherKey)).replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
