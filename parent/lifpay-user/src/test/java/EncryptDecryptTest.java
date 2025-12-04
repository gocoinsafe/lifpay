
import org.hcm.lifpay.util.AESCBCUtils;
import org.hcm.lifpay.util.RSASignature;
import org.hcm.lifpay.util.SensitiveInfoUtil;

import java.util.HashMap;
import java.util.Map;

public class EncryptDecryptTest {




    // 模拟前端加密过程
//    @Test
    public static void testFrontendEncryptAndBackendDecrypt() throws Exception {

        // 您提供的密钥信息
        String SIGN_PUBLIC_KEY = "04f37377c97eefebf5c3ee7b5003b6b7c105f8fe8b07985e18c4f8daac75c712e3b0bf0da2797fa3788366db7cbc11d6eac179d3833f2763dbf3b857f92cd961ac";
         String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvyV0ArzIp7PkJU/jpK2dLYW+i644/N3aOd7fX6Gtc2alCD7gu9XbFOI6WvVsiQYaaxWcOGS+6oVEX3t8pr0yw3f/Pj35OC5co+Z2qAsdC1VgmC40+ezg7BlDTSGEfRLhmT+myY453h83+psLzYREJvPEJ2TZpLfPMx60kvhSLnlQmGWfhWOG3Su0NPLZSiLcwljw46jYjuL7f5BicjHh54a9XgZAZGESkc1Mvs0yKS+O+z75shsw4xOhab7hju7b0C3oM/l8ttxlMNIfn2MnBkii9JcSmoRiY3HN+7F6k2vC7zESuOpeucz7DnSiImKH4xIIW++MjKQ3g6YFO+qkPAgMBAAECggEAVaTrtPummxqjstHAwgmth+Ju+5d8dHxXPM5DOeQ4+sHpXxA9klNxjGEjx3l2P1hhdmKa9Rl8UZdq5RBm9e48lSd0DPJCfU/UU9hZ4oz7/Cnpio+yPzDVkpxfILFw8EUQ+ngQyiMLNF1sFZe1/9HY9T+iyEOV6OTElY7rYq1hYSvwCrD+sxfk4zBRjV+eG8PDdS1RBrQwJi5ecjaDgnVHdTX37JdfwFxl6CmAXkuLMwq0W+HNNXyPR194UuDXuAg86Q1Y84SA4mgDKbSU8kwUec+KakhVcAIxDN5x9XrsKg2hcCTyct8K6sljIROcfh9//udmBhW3F1bzuqaxwu+wMQKBgQDyqxDVPtshWFkM9oyzwlAULvyZGUL6Cq/Fgge0qUA5qkQzGGfO15wt2vYqKiEQo2qZZ0+OsjNNd6VDfUDgpyRLBNsLe+zXXXLdXQdNE9vyiB8wiV/9igTFuh28V7icW6KErgghKKCLm7vOK3u7ZYTyapcfyQdVG/rNB6F0e5tgzQKBgQC5cakjC8G5a0QhHQMTa9KYgwNWu30KJUQCnT1npY2vZ8bQje9Gdhg4bJYfYuVlXSfRRbNUX6pqRzPyZAtut0jDExsOnzMjBporZ4WgPi4DnEHR+d0ovyENt+Viqje9DP6KHxCfDrzghfEy51JmoQAbIaggBQekpSRRrDgwJ2aBSwKBgQCfLK6B8hTyrmzbH+3zC4ZTdu6hzfws302klEJRsqM6MAFEMsIE31DGk1XdGn1N2KNXtHhu9VzJd62js0kXEwuvWaQEyGj2a4mowhjD6j7fu4IZ6EJaoGm4+PgATtn9Ve4oca9LcMa+TIzIE6W5qkGmcVxnsQlqPkwoqNSy/1FQEQKBgAEB4Q1TrwW203PqmG2ulEJH0+jQs+kuMqRg/khl2dMuGSJg4v/a/F6yGE6rVtuqGeFFI6g9rMtO/7U9XeKIFFka7Xay3rA+BPBfa5ZnQBC89I6TcOQbxa4xZYmXqx3XyDov2QNELTp6/8hrAUOVdE6xbBxgap42V3AqI1P/osrpAoGAXDWnR2oWnuCxFcUiwSe9/XSHfbYqWZBd35Y5e24hMBkyUdqZKIkMI4jIGvZ2/3FiKSPqF+uhbnKiT7JIK0TIs6l2IrJ46Xh9j6IQCRmglfKbZMgQMAvJQpSl4Nn+sow2MXMGOcLcPt85tL1e4AsImLXywwlMtf4btW1ALZoL8Bg=";

        // 1. 前端准备原始数据
        String username = "testUser";
        String plainPassword = "testPassword123"; // 明文密码
        String frontendPublicKey = "frontendPublicKey123"; // 前端公钥（模拟）
        long timestamp = System.currentTimeMillis(); // 时间戳
        String requestId = "req-" + timestamp; // 请求ID

        // 2. 前端生成AES密钥（实际场景中应为随机生成，这里复用后端配置的publicKey模拟）
        String aesKey = SIGN_PUBLIC_KEY; // 对应代码中`String aesKey = publicKey`
        String aesKey16 = aesKey.substring(0, 16); // AES密钥取前16位

        // 3. 前端加密privateContent（格式：后端rsaPrivateKey&&timestamp）
        String privateContentPlain = RSA_PRIVATE_KEY + "&&" + timestamp;
        String privateContentEncrypted = AESCBCUtils.encrypt(privateContentPlain, aesKey16);
        System.out.println("前端加密后的privateContent: " + privateContentEncrypted);

        // 4. 前端加密password（格式：前端公钥&密码，用RSA公钥加密后拆分）
        // 4.1 拼接原始密码内容
        String passwordPlain = frontendPublicKey + "&" + plainPassword;
        // 4.2 用RSA公钥加密（注意：实际前端应使用与后端rsaPrivateKey对应的公钥，这里假设已获取）
        // （如果没有RSA公钥，可从私钥推导，此处省略推导过程，直接使用加密逻辑）
        String passwordEncryptedPart1 = RSASignature.doEncrypt(passwordPlain.substring(0, passwordPlain.length()/2), "RSA_PUBLIC_KEY"); // 替换为实际RSA公钥
        String passwordEncryptedPart2 = RSASignature.doEncrypt(passwordPlain.substring(passwordPlain.length()/2), "RSA_PUBLIC_KEY"); // 替换为实际RSA公钥
        String passwordEncrypted = passwordEncryptedPart1 + "&" + passwordEncryptedPart2;
        System.out.println("前端加密后的password: " + passwordEncrypted);

        // 5. 前端加密username（使用AES密钥）
        String usernameEncrypted = SensitiveInfoUtil.apiEncrypt(username, aesKey); // 假设apiEncrypt是AES加密
        System.out.println("前端加密后的username: " + usernameEncrypted);

        // ------------------------------
        // 模拟后端解密过程（复用LoginServiceImpl中的解密逻辑）
        // ------------------------------
        // 6. 后端从请求中获取参数
        String reqPrivateContent = privateContentEncrypted;
        String reqPassword = passwordEncrypted;
        String reqAesKey = aesKey;
        String reqUsername = usernameEncrypted;

        // 7. 后端解密privateContent（获取RSA私钥）
        String privateKeyStr = decryptPrivateKey(reqPrivateContent, aesKey16);
        System.out.println("后端解密得到的RSA私钥: " + privateKeyStr.substring(0, 50) + "..."); // 打印前50位

        // 8. 后端解密password（获取明文密码和前端公钥）
        Map<String, String> decryptedMap = decryptPublicKey(reqPassword, privateKeyStr);
        String decryptedPassword = decryptedMap.get("password");
        String decryptedPublicKey = decryptedMap.get("publicKey");
        System.out.println("后端解密得到的明文密码: " + decryptedPassword);
        System.out.println("后端解密得到的前端公钥: " + decryptedPublicKey);

        // 9. 后端解密username
        String decryptedUsername = SensitiveInfoUtil.apiDecrypt(reqUsername, reqAesKey);
        System.out.println("后端解密得到的username: " + decryptedUsername);

        // 10. 验证解密结果
        assert decryptedPassword.equals(plainPassword) : "密码解密失败";
        assert decryptedPublicKey.equals(frontendPublicKey) : "前端公钥解密失败";
        assert decryptedUsername.equals(username) : "用户名解密失败";
        System.out.println("所有解密验证通过！");
    }

    // 复用LoginServiceImpl中的解密方法
    private static String decryptPrivateKey(String privateContent, String aesKey) throws Exception {
        String privateStr = AESCBCUtils.decrypt(privateContent, aesKey);
        String privateKey = privateStr.split("&&")[0];
        if (privateKey == null) {
            throw new RuntimeException("decryptPrivateKey failed");
        }
        return privateKey;
    }

    // 复用LoginServiceImpl中的解密方法
    private static Map<String, String> decryptPublicKey(String reqCode, String privateKey) throws Exception {
        Map<String, String> map = new HashMap<>(2);
        String[] parts = reqCode.split("&");
        if (parts.length != 2) {
            throw new RuntimeException("invalid password format");
        }
        String decPart1 = RSASignature.doDecrypt(parts[0], privateKey);
        String decPart2 = RSASignature.doDecrypt(parts[1], privateKey);

        String[] decPart1Split = decPart1.split("&");
        if (decPart1Split.length != 2) {
            throw new RuntimeException("invalid decrypted part1 format");
        }
        String publicKey = decPart1Split[0];
        String password = decPart1Split[1] + decPart2;

        map.put("password", password);
        map.put("publicKey", publicKey);
        return map;
    }


    public static void main(String[] args) throws Exception {
        testFrontendEncryptAndBackendDecrypt();
    }

}
