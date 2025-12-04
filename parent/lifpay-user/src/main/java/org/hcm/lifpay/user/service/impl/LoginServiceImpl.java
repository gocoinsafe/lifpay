package org.hcm.lifpay.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.Constants;
import org.hcm.lifpay.redis.RedisDBKey;
import org.hcm.lifpay.user.dto.UserResultEnum;
import org.hcm.lifpay.user.dto.req.LoginRequest;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.exception.LifpayException;
import org.hcm.lifpay.user.service.LoginService;
import org.hcm.lifpay.util.AESCBCUtils;
import org.hcm.lifpay.util.RSASignature;
import org.hcm.lifpay.util.SensitiveInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Service
@Slf4j
@RefreshScope
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);


    private static final String PASSWORD = "password";

    private static final String PUBLIC_KEY = "publicKey";


    private static final String VALUE = "0x";

    @Value("${admin.secure}")
    boolean secure;
//
    @Value("${system.sensitiveCipherKey}")
    String sensitiveCipherKey;

    @Autowired
    protected RedisDS redisDS;

    @Value("${tokenExpireTime.token_Expire_Time}")
    int tokenValidTime;
//
//    @Value("${tokenExpireTime.refresh_token_Expire_Time}")
//    int refreshTokenValidTime;
//
    @Value("${admin.domain}")
    String domain;

    // 你的配置密钥（和网关层配置一致）
    // === 你的 RSA 公钥（推导出的）===
    private static final String RSA_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr8ldAK8yKez5CVP46StnS2FvouuOPzd2jne31+hrXNmpQg+4LvV2xTiOlr1bIkGGmsVnDhkvuqFRF97fKa9MsN3/z49+TguXKPmdqgLHQtVYJguNPns4OwZQ00hhH0S4Zk/psmOOd4fN/qbC82ERCbzxCdk2aS3zzMetJL4Ui55UJhln4Vjht0rtDTy2Uoi3MJY8OOo2I7i+3+QYnIx4eeGvV4GQGRhEpHNTL7NMikvjvs++bIbMOMToWm+4Y7u29At6DP5fLbcZTDSH59jJwZIovSXEpqEYmNxzfuxepNrwu8xErjqXrnM+w50oiJih+MSCFvvjIykN4OmBTvqpDwIDAQAB";

    // === 你的 RSA 私钥（业务层实际使用的）===
    private static final String RSA_PRIVATE_KEY =
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvyV0ArzIp7PkJU/jpK2dLYW+i644/N3aOd7fX6Gtc2alCD7gu9XbFOI6WvVsiQYaaxWcOGS+6oVEX3t8pr0yw3f/Pj35OC5co+Z2qAsdC1VgmC40+ezg7BlDTSGEfRLhmT+myY453h83+psLzYREJvPEJ2TZpLfPMx60kvhSLnlQmGWfhWOG3Su0NPLZSiLcwljw46jYjuL7f5BicjHh54a9XgZAZGESkc1Mvs0yKS+O+z75shsw4xOhab7hju7b0C3oM/l8ttxlMNIfn2MnBkii9JcSmoRiY3HN+7F6k2vC7zESuOpeucz7DnSiImKH4xIIW++MjKQ3g6YFO+qkPAgMBAAECggEAVaTrtPummxqjstHAwgmth+Ju+5d8dHxXPM5DOeQ4+sHpXxA9klNxjGEjx3l2P1hhdmKa9Rl8UZdq5RBm9e48lSd0DPJCfU/UU9hZ4oz7/Cnpio+yPzDVkpxfILFw8EUQ+ngQyiMLNF1sFZe1/9HY9T+iyEOV6OTElY7rYq1hYSvwCrD+sxfk4zBRjV+eG8PDdS1RBrQwJi5ecjaDgnVHdTX37JdfwFxl6CmAXkuLMwq0W+HNNXyPR194UuDXuAg86Q1Y84SA4mgDKbSU8kwUec+KakhVcAIxDN5x9XrsKg2hcCTyct8K6sljIROcfh9//udmBhW3F1bzuqaxwu+wMQKBgQDyqxDVPtshWFkM9oyzwlAULvyZGUL6Cq/Fgge0qUA5qkQzGGfO15wt2vYqKiEQo2qZZ0+OsjNNd6VDfUDgpyRLBNsLe+zXXXLdXQdNE9vyiB8wiV/9igTFuh28V7icW6KErgghKKCLm7vOK3u7ZYTyapcfyQdVG/rNB6F0e5tgzQKBgQC5cakjC8G5a0QhHQMTa9KYgwNWu30KJUQCnT1npY2vZ8bQje9Gdhg4bJYfYuVlXSfRRbNUX6pqRzPyZAtut0jDExsOnzMjBporZ4WgPi4DnEHR+d0ovyENt+Viqje9DP6KHxCfDrzghfEy51JmoQAbIaggBQekpSRRrDgwJ2aBSwKBgQCfLK6B8hTyrmzbH+3zC4ZTdu6hzfws302klEJRsqM6MAFEMsIE31DGk1XdGn1N2KNXtHhu9VzJd62js0kXEwuvWaQEyGj2a4mowhjD6j7fu4IZ6EJaoGm4+PgATtn9Ve4oca9LcMa+TIzIE6W5qkGmcVxnsQlqPkwoqNSy/1FQEQKBgAEB4Q1TrwW203PqmG2ulEJH0+jQs+kuMqRg/khl2dMuGSJg4v/a/F6yGE6rVtuqGeFFI6g9rMtO/7U9XeKIFFka7Xay3rA+BPBfa5ZnQBC89I6TcOQbxa4xZYmXqx3XyDov2QNELTp6/8hrAUOVdE6xbBxgap42V3AqI1P/osrpAoGAXDWnR2oWnuCxFcUiwSe9/XSHfbYqWZBd35Y5e24hMBkyUdqZKIkMI4jIGvZ2/3FiKSPqF+uhbnKiT7JIK0TIs6l2IrJ46Xh9j6IQCRmglfKbZMgQMAvJQpSl4Nn+sow2MXMGOcLcPt85tL1e4AsImLXywwlMtf4btW1ALZoL8Bg="; // 省略，使用你完整那段

    // === 网关提供的 AES Key（你 CheckGlobalFilter 中的 publicKey）===
    private static final String AES_KEY = "04f37377c97eefebf5c3ee7b5003b6b7c105f8fe8b07985e18c4f8daac75c712e3b0bf0da2797fa3788366db7cbc11d6eac179d3833f2763dbf3b857f92cd961ac"; // 示例，你用自己的



    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<LoginResponse> login(LoginRequest request, HttpServletResponse httpServletResponse) {
        logger.info("starting to do login verification.");
        BaseResponse<LoginResponse> response = new BaseResponse<>();

        try {
            if (StringUtils.isEmpty(request.getUsername()) || StringUtils.isEmpty(request.getPassword())) {
                throw new LifpayException(UserResultEnum.BAD_INPUT.getCode(), UserResultEnum.BAD_INPUT.getMsg());
            }
            String aesKey = request.getAesKey().substring(0, 16);
            // AES解密出RSA私钥
            String privateKey = decryptPrivateKey(request.getPrivateContent(), aesKey);
            // RSA解密
            Map<String, String> codeMap = decryptPublicKey(request.getPassword(), privateKey);
            // 明文
            String plainPwd = codeMap.get(PASSWORD);
            String publicKey = codeMap.get(PUBLIC_KEY);
            // 本地加密与数据库对比
            String localEncPwd = SensitiveInfoUtil.encrypt(plainPwd, sensitiveCipherKey);
            logger.info("codeMap: {}", JSONObject.toJSONString(codeMap));

            String decUserName = SensitiveInfoUtil.apiDecrypt(request.getUsername(), request.getAesKey());
//            QueryWrapper<AdminUserDO> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda()
//                    .eq(AdminUserDO::getUserName, decUserName)
//                    .eq(AdminUserDO::getPassword, localEncPwd);
//            AdminUserDO user = adminUserDao.getOne(queryWrapper);
//            if (null == user) {
//                throw new LifpayException(UserResultEnum.BAD_COMBINATION.getCode(),
//                        UserResultEnum.BAD_COMBINATION.getMsg());
//            }
            LoginResponse loginResponse = new LoginResponse();
//            loginResponse.setUserId(user.getId());
//            loginResponse.setUsername(user.getUserName());
            // 缓存token
            String token = getToken(user.getId(), user.getUserName(), publicKey);
            // 缓存refresh token
//            String refreshToken = generateRefreshToken(user.getId(), user.getUserName(), publicKey);
            // 缓存用户信息
//            cacheUserInfo(user);
//            loginResponse.setRefreshToken(refreshToken);
            response.setData(loginResponse);
            //放token到cookie
            httpServletResponse.addCookie(createCookie(Constants.TOKEN_NAME, "", -1, domain, true));
            response.setCode(UserResultEnum.SUCCESS.getCode());
            response.setMessage(UserResultEnum.SUCCESS.getMsg());
        } catch (LifpayException e) {
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
            response.setData(null);
            log.error("login fail: {} - {}", e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            response.setCode(UserResultEnum.SYSTEM_INTERNAL_ERROR.getCode());
            response.setMessage(UserResultEnum.SYSTEM_INTERNAL_ERROR.getMsg());
            logger.error("system internal error", e);
        }
        logger.info("done doing verification");
        return response;
    }

//    private void cacheUserInfo(AdminUserDO adminUserDO) {
//        // 从数据库获取用户信息
//        AdminUserInfo adminUserInfo = adminUserDao.getAdminUserInfoById(adminUserDO.getId());
//        AuthUtil.setUserInfoCache(adminUserInfo, tokenValidTime);
//    }

    private Cookie createCookie(String key, String value, int maxAge, String domain, boolean httpOnly) {
        logger.info("create cookie :{}；{}；{}；{}； {}", key, value, maxAge, domain, httpOnly);
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath("/");
        cookie.setSecure(secure);
        return cookie;
    }


    /**
     * 生成refresh token
     *
     * @param userId 用户id deviceId 设备号
     * @return refresh token
     */
//    private String generateRefreshToken(long userId, String deviceId, String publicKey) {
//        String key = getRefreshTokenKey(String.valueOf(userId), getUserDeviceIdFrmPubKey(publicKey));
//        String refreshToken = createToken();
//        logger.info("start generateRefreshToken:{}", refreshToken);
//        //设置refresh token 有效时间
//        redisDS.setex(key, refreshToken, refreshTokenValidTime);
//        String tokenKey = String.format(RedisDBKey.GET_ADMINUSER_ID_BY_REFRESH_TOKEN, refreshToken);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userId", userId);
//        jsonObject.put("deviceId", deviceId);
//        jsonObject.put("timestamp", System.currentTimeMillis());
//        jsonObject.put("publicKey", publicKey);
//        redisDS.setex(tokenKey, jsonObject.toJSONString(), refreshTokenValidTime);
//        logger.info("end generateRefreshToken:{}", jsonObject.toJSONString());
//        return refreshToken;
//    }

    private String getRefreshTokenKey(String userId, String deviceId) {
        return String.format(RedisDBKey.GET_REFRESH_TOKEN_BY_USER, userId, deviceId);
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }

    private String getTokenKey(String userId, String deviceId) {
        return String.format(RedisDBKey.GET_TOKEN_BY_USER, userId, deviceId);
    }

    private String getUserNameTokenKey(String userId) {
        return String.format(RedisDBKey.GET_USERNAME_BY_USERID, userId);
    }

    private Map<String, String> decryptPublicKey(String reqCode, String privateKey) {
        Map<String, String> map = new HashMap<>(2);
        String decPart1;
        String decPart2;
        try {
            String part1 = reqCode.split("&")[0];
            String part2 = reqCode.split("&")[1];
            decPart1 = RSASignature.doDecrypt(part1, privateKey);
            decPart2 = RSASignature.doDecrypt(part2, privateKey);
        } catch (Exception e) {
            log.error("Fail:", e);
            throw new LifpayException(UserResultEnum.BAD_COMBINATION.getCode(), UserResultEnum.BAD_COMBINATION.getMsg());
        }
        String pwd = decPart1.split("&")[1] + decPart2;
        String publicKey = decPart1.split("&")[0];
        if (publicKey == null) {
            log.error("decryptPublicKey fail {}", reqCode);
            throw new LifpayException(UserResultEnum.BAD_COMBINATION.getCode(), UserResultEnum.BAD_COMBINATION.getMsg());
        }
        map.put(PASSWORD, pwd);
        map.put(PUBLIC_KEY, publicKey);
        return map;
    }
//
    private String decryptPrivateKey(String privateContent, String aesKey) {
        String privateStr = AESCBCUtils.decrypt(privateContent, aesKey);
        String privateKey = privateStr.split("&&")[0];
        if (privateKey == null) {
            log.error("decryptPrivateKey fail {}", privateContent);
            throw new LifpayException(UserResultEnum.BAD_COMBINATION.getCode(), UserResultEnum.BAD_COMBINATION.getMsg());
        }
        return privateKey;
    }
//
//    /**
//     * //从redis获取refresh_token
//     *
//     * @param userId 用户id
//     * @return refresh_token
//     */
//    private String getRefreshToken(long userId, String deviceId) {
//        String key = getRefreshTokenKey(String.valueOf(userId), deviceId);
//        return redisDS.getStr(key);
//    }
//
//    /**
//     * //从redis获取token
//     *
//     * @param userId 用户id
//     * @return token
//     */
//    private String getTokenValue(long userId, String deviceId) {
//        String key = getTokenKey(String.valueOf(userId), deviceId);
//        return redisDS.getStr(key);
//    }
//
//    /**
//     * //延长refresh token有效期
//     *
//     * @param userId 用户id deviceId 设备号
//     *               value app 发送过来的refresh token
//     */
//    private void extensionRefreshToken(long userId, String deviceId, String value) {
//        String key = getRefreshTokenKey(String.valueOf(userId), deviceId);
//        redisDS.setex(key, value, refreshTokenValidTime);
//        String refreshTokenKey = String.format(RedisDBKey.GET_ADMINUSER_ID_BY_REFRESH_TOKEN, value);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userId", userId);
//        jsonObject.put("deviceId", deviceId);
//        jsonObject.put("timestamp", System.currentTimeMillis());
//        redisDS.setex(refreshTokenKey, jsonObject.toJSONString(), refreshTokenValidTime);
//    }
//
//    /**
//     * //延长用户 token有效期
//     *
//     * @param userId 用户id deviceId 设备号
//     *               value app 发送过来的refresh token
//     */
//    private void extensionUserToken(long userId, String userName, String deviceId, String value, String publicKey) {
//        String key = getTokenKey(String.valueOf(userId), deviceId);
//        redisDS.setex(key, value, tokenValidTime);
//        log.info("extensionUserToken: {}", value);
//        String tokenKey = String.format(RedisDBKey.GET_ADMINUSER_ID_BY_TOKEN, value);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userId", userId);
//        jsonObject.put("deviceId", deviceId);
//        jsonObject.put("timestamp", System.currentTimeMillis());
//        jsonObject.put("publicKey", publicKey);
//        redisDS.setex(tokenKey, jsonObject.toJSONString(), tokenValidTime);
//        String userNameKey = getUserNameTokenKey(String.valueOf(userId));
//        redisDS.setex(userNameKey, userName, tokenValidTime);
//    }

    private String getToken(long userId, String userName, String publicKey) {
        logger.info("start to get token from Redis");
//		int tokenExpiredDay = 3;
//		int tokenExpiredSeconds = tokenExpiredDay * 60 * 60;
//		logger.info(String.format("Expired time: %s day", tokenExpiredDay));
        // token的key: userId + deviceId
        String key = getTokenKey(String.valueOf(userId), getUserDeviceIdFrmPubKey(publicKey));
        String token = redisDS.getStr(key);

        if (token == null) {
            token = createToken();
            redisDS.setex(key, token, tokenValidTime);
            String tokenKey = String.format(RedisDBKey.GET_ADMINUSER_ID_BY_TOKEN, token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("publicKey", publicKey);
            jsonObject.put("timestamp", System.currentTimeMillis());
            redisDS.setex(tokenKey, jsonObject.toJSONString(), tokenValidTime);
            String userNameKey = getUserNameTokenKey(String.valueOf(userId));
            redisDS.setex(userNameKey, userName, tokenValidTime);
        }
        logger.info("finished getting token from Redis,final token :{}", token);
        return token;
    }



    public void testLoginEncryptDecrypt() throws Exception {
        // ========= 1. 模拟前端准备数据 =========
        String clientPublicKey = "client_public_key_xxx"; // 客户端生成的公钥（示例）
        String plainPassword = "123456"; // 明文密码

        // 关键：按后端预期拆分数据为两部分（这里简单按长度拆分，实际前端需保持一致）
        int splitIndex = plainPassword.length() / 2;
        String passwordPart1 = plainPassword.substring(0, splitIndex); // 密码前半段
        String passwordPart2 = plainPassword.substring(splitIndex);     // 密码后半段

        // part1明文 = 客户端公钥 + "&" + 密码前半段（后端解密后需要提取publicKey）
        String part1Plain = clientPublicKey + "&" + passwordPart1;
        // part2明文 = 密码后半段
        String part2Plain = passwordPart2;

        // ========= 2. 前端用RSA公钥加密两部分 =========
        String part1Enc = RSASignature.doEncrypt(part1Plain, RSA_PUBLIC_KEY); // 加密part1
        String part2Enc = RSASignature.doEncrypt(part2Plain, RSA_PUBLIC_KEY); // 加密part2

        // 前端传入的password = 两部分密文用&拼接（核心修正点）
        String reqPassword = part1Enc + "&" + part2Enc;

        // ========= 3. 模拟网关生成privateContent =========
        String timestamp = String.valueOf(System.currentTimeMillis());
        String privateContentStr = RSA_PRIVATE_KEY + "&&" + timestamp; // 私钥+时间戳
        String privateContent = AESCBCUtils.encrypt(privateContentStr, AES_KEY.substring(0, 16)); // AES加密

        // ========= 4. 构造LoginRequest =========
        LoginRequest req = new LoginRequest();
        req.setUsername(RSASignature.doEncrypt("testUser", RSA_PUBLIC_KEY)); // 用户名加密（示例）
        req.setPassword(reqPassword); // 关键：使用&拼接的密文
        req.setAesKey(AES_KEY);
        req.setPrivateContent(privateContent);

        // ========= 5. 后端解密流程（复用现有代码） =========
        String aesKey = req.getAesKey().substring(0, 16);
        String privateKey = decryptPrivateKey(req.getPrivateContent(), aesKey); // 解密得到RSA私钥
        Map<String, String> codeMap = decryptPublicKey(req.getPassword(), privateKey); // 现在split不会报错

        // ========= 6. 验证结果 =========
        System.out.println("解密得到的明文密码: " + codeMap.get(PASSWORD)); // 应输出123456
        System.out.println("解密得到的客户端公钥: " + codeMap.get(PUBLIC_KEY)); // 应输出client_public_key_xxx
    }


}
