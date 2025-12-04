package org.hcm.lifpay.util;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinzhe
 */
public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA256withRSA";
    public static final String KEY_ALGORITHMS = "RSA";
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String ECB_OAEP_PADDING = "RSA/ECB/OAEPPadding";
    public static final String ECB_OAEP_SHA1_MGF1_PADDING = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    public static final String ECB_OAEP_SHA256_MGF1_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final Logger log = LoggerFactory.getLogger(RSASignature.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHMS);
            PrivateKey priKey = keyf.generatePrivate(priPkcs8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(sha256(content));
            byte[] signed = signature.sign();

            return Base64.toBase64String(signed);
        } catch (Exception e) {
            log.error("Fail",e);
        }
        return null;
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(sha256(content));

            return signature.verify(Base64.decode(sign));

        } catch (Exception e) {
            log.error("Fail",e);
        }

        return false;
    }

    public static String doEncrypt(String plainText, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Cipher cipher = Cipher.getInstance(ECB_OAEP_PADDING);
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, oaepParams);
        byte[] data = plainText.getBytes(DEFAULT_CHARSET);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // OAEP填充：密钥长度 - 2 - hash长度*2
        int encryptBlockSize = ((RSAKey) pubKey).getModulus().bitLength() / 8 - 2 - 256 / 8 * 2;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > encryptBlockSize) {
                cache = cipher.doFinal(data, offSet, encryptBlockSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * encryptBlockSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        return Base64.toBase64String(encryptedData);
    }

    public static String doDecrypt(String cipherTextBase64, String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
        byte[] encodedKey = Base64.decode(privateKey);
        PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        Cipher cipher = Cipher.getInstance(ECB_OAEP_PADDING);
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, priKey, oaepParams);
        byte[] encryptedData = Base64.decode(cipherTextBase64.getBytes(DEFAULT_CHARSET));
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        int decryptBlockSize = cipher.getOutputSize(0);
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > decryptBlockSize) {
                cache = cipher.doFinal(encryptedData, offSet, decryptBlockSize);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * decryptBlockSize;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();

        return new String(decryptedData, DEFAULT_CHARSET);

    }

    public static byte[] sha256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
    }

    public static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(16);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获得公钥
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return Base64.toBase64String(key.getEncoded());
    }

    /**
     * 获得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return Base64.toBase64String(key.getEncoded());
    }

    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            log.warn(publicKey);
            String privateKey = getPrivateKey(keyMap);
            log.warn(privateKey);
        } catch (Exception e) {
            log.error("Fail",e);
        }
    }

    public static byte[] getPubKeyFrmPrvKey(byte[] prvKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey privateKey =
                (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHMS).generatePrivate(new PKCS8EncodedKeySpec(prvKey));
        RSAPrivateCrtKey rsaPrvKey = (RSAPrivateCrtKey) privateKey;

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrvKey.getModulus(), rsaPrvKey.getPublicExponent());

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHMS);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey.getEncoded();
    }

    public static String getPubKeyFrmPrvKey(String privateKeyStr) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] prvKey = Base64.decode(privateKeyStr);
        byte[] pubKey = getPubKeyFrmPrvKey(prvKey);
        return Base64.toBase64String(pubKey);
    }

}
