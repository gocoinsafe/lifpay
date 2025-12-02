package org.hcm.lifpay.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

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

@Slf4j
public class AESCBCUtils {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 分组模式为CBC，填充模式为PKCS7Padding，即PKCS5Padding
     */
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    /**
     * 向量
     */
    public static final String IV_SEED = "1234567812345678";
    public static final int KEY_LEN = 16;

    /**
     * AES加密算法
     *
     * @param str 密文
     * @param key 密key
     * @return
     */
    public static String encrypt(String str, String key) {
        return encrypt(str, key, IV_SEED);
    }

    /**
     * AES加密算法
     *
     * @param data 待加密数据
     * @param key  密钥key
     * @param iv   向量
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        if (data == null) {
            log.info("AES加密出错:Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length != KEY_LEN) {
            log.info("AES加密出错:Key长度不是16位");
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParam = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            return cipher.doFinal(data);
        } catch (Exception ex) {
            log.warn("AES加密出错 ex={}", ex);
            throw new RuntimeException("encrypt fail!", ex);
        }
    }

    public static String encrypt(String str, String key, String ivSeed) {
        String encoding = StandardCharsets.UTF_8.toString();
        byte[] encrypted = null;
        try {
            encrypted = encrypt(str.getBytes(encoding), key.getBytes(encoding), ivSeed.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        if (encrypted == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * @param str 密文
     * @param key 密key
     * @return
     */
    public static String decrypt(String str, String key) {
        return decrypt(str, key, IV_SEED);
    }

    /**
     * AES解密算法
     *
     * @param encryptedData 密文
     * @param key           密key
     * @param iv            向量
     * @return
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) {
        // 判断Key是否正确
        if (key == null) {
            log.info("AES解密出错:Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length != KEY_LEN) {
            log.info("AES解密出错：Key长度不是16位");
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            //设置为解密模式
            IvParameterSpec ivParam = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            log.warn("AES解密出错 ex={}", e);
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String decrypt(String str, String key, String ivSeed) {
        String encoding = StandardCharsets.UTF_8.toString();
        byte[] encrypted = Base64.getDecoder().decode(str);
        byte[] origin = null;
        try {
            origin = decrypt(encrypted, key.getBytes(encoding), ivSeed.getBytes(encoding));
        } catch (IOException ex) {
            log.info("AES解密出错ex={}", ex);
            return null;
        }
        String result = null;
        try {
            result = new String(origin, encoding);
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        if (result == null) {
            return null;
        }
        return result;
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     * @deprecated use Hex.encodeHexString instead
     */
    public static String byteToHexString(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    /**
     * 随机生成秘钥
     *
     * @deprecated
     */
    public static String getAesKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String str = byteToHexString(b);
            return str;
        } catch (NoSuchAlgorithmException e) {
            log.info("getAesKey error e={}", e);
            return "defaultaes";
        }
    }

}
