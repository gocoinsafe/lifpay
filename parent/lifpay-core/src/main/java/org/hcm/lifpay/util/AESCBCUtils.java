package org.hcm.lifpay.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;
import org.apache.commons.codec.binary.Hex;

public class AESCBCUtils {

    private static final Logger log = LoggerFactory.getLogger(AESCBCUtils.class);
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    public static final String IV_SEED = "1234567812345678";
    public static final int KEY_LEN = 16;

    public AESCBCUtils() {
    }

    public static String encrypt(String str, String key) {
        return encrypt(str, key, "1234567812345678");
    }

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        if (data == null) {
            log.info("AES加密出错:Key为空null");
            return null;
        } else if (key.length != 16) {
            log.info("AES加密出错:Key长度不是16位");
            return null;
        } else {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                IvParameterSpec ivParam = new IvParameterSpec(iv);
                cipher.init(1, secretKey, ivParam);
                return cipher.doFinal(data);
            } catch (Exception var6) {
                log.warn("AES加密出错 ex={}", var6);
                throw new RuntimeException("encrypt fail!", var6);
            }
        }
    }

    public static String encrypt(String str, String key, String ivSeed) {
        String encoding = StandardCharsets.UTF_8.toString();
        byte[] encrypted = null;

        try {
            encrypted = encrypt(str.getBytes(encoding), key.getBytes(encoding), ivSeed.getBytes(encoding));
        } catch (UnsupportedEncodingException var6) {
        }

        return encrypted == null ? null : Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String str, String key) {
        return decrypt(str, key, "1234567812345678");
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) {
        if (key == null) {
            log.info("AES解密出错:Key为空null");
            return null;
        } else if (key.length != 16) {
            log.info("AES解密出错：Key长度不是16位");
            return null;
        } else {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                IvParameterSpec ivParam = new IvParameterSpec(iv);
                cipher.init(2, secretKey, ivParam);
                return cipher.doFinal(encryptedData);
            } catch (Exception var6) {
                log.warn("AES解密出错 ex={}", var6);
                throw new RuntimeException("decrypt fail!", var6);
            }
        }
    }

    public static String decrypt(String str, String key, String ivSeed) {
        String encoding = StandardCharsets.UTF_8.toString();
        byte[] encrypted = Base64.getDecoder().decode(str);
        Object var5 = null;

        byte[] origin;
        try {
            origin = decrypt(encrypted, key.getBytes(encoding), ivSeed.getBytes(encoding));
        } catch (IOException var9) {
            log.info("AES解密出错ex={}", var9);
            return null;
        }

        String result = null;

        try {
            result = new String(origin, encoding);
        } catch (UnsupportedEncodingException var8) {
        }

        return result == null ? null : result;
    }

    /** @deprecated */
    public static String byteToHexString(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    /** @deprecated */
    public static String getAesKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String str = byteToHexString(b);
            return str;
        } catch (NoSuchAlgorithmException var4) {
            log.info("getAesKey error e={}", var4);
            return "defaultaes";
        }
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
