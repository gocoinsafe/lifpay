package org.hcm.lifpay.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.Constants;
import org.hcm.lifpay.common.DigitalResultEnum;
import org.hcm.lifpay.dto.UserInfo;
import org.hcm.lifpay.misc.MiscClient;
import org.hcm.lifpay.misc.common.VerifyCodeTypeEnum;
import org.hcm.lifpay.misc.req.InnerGetVerifyCodeReq;
import org.hcm.lifpay.misc.resp.GetVerifyCodeResp;
import org.hcm.lifpay.redis.RedisDBKey;
import org.hcm.lifpay.redis.RedisDS;
import org.hcm.lifpay.user.constant.UserStatusEnum;
import org.hcm.lifpay.user.constant.UserTypeEnum;
import org.hcm.lifpay.user.dao.entity.UserInfoDo;
import org.hcm.lifpay.user.dao.repository.UserInfoRepository;
import org.hcm.lifpay.user.dto.UserResultEnum;
import org.hcm.lifpay.user.dto.req.*;
import org.hcm.lifpay.user.dto.resp.LoginResponse;
import org.hcm.lifpay.user.dto.resp.RefreshTokenResDto;
import org.hcm.lifpay.user.dto.resp.UserInfoResp;
import org.hcm.lifpay.user.exception.LifpayException;
import org.hcm.lifpay.user.remote.MiscRemoteService;
import org.hcm.lifpay.user.service.UserLoginService;
import org.hcm.lifpay.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Service
@Slf4j
@RefreshScope
public class UserLoginServiceImpl implements UserLoginService {

    private final static Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);


    @Autowired
    private UserInfoRepository userInfoRepository;


    @Autowired
    protected MiscRemoteService miscRemoteService;


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

    @Value("${tokenExpireTime.refresh_token_Expire_Time}")
    int refreshTokenValidTime;

    @Value("${admin.domain}")
    String domain;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        logger.info("starting to do login verification.");
        // 1. 参数基础校验
        if (StringUtils.isEmpty(request.getContact()) || StringUtils.isEmpty(request.getPassword()) ||
                StringUtils.isEmpty(request.getVerifyCode()) || request.getType() == null) {
            return BaseResponse.fail(DigitalResultEnum.PARAM_ERROR);
        }

        // 2. 枚举与联系方式格式校验
        VerifyCodeTypeEnum typeEnum = VerifyCodeTypeEnum.getByType(request.getType());
        if (typeEnum == null) {
            return BaseResponse.fail(DigitalResultEnum.PARAM_ERROR.getCode(), "联系方式类型不合法");
        }
//        if (typeEnum == VerifyCodeTypeEnum.PHONE && !RegexUtils.isMobile(request.getContact())) {
//            return BaseResponse.fail(DigitalResultEnum.PARAM_ERROR.getCode(), "手机号格式错误");
//        }
        if (typeEnum == VerifyCodeTypeEnum.EMAIL && !RegexUtils.isEmail(request.getContact())) {
            return BaseResponse.fail(DigitalResultEnum.PARAM_ERROR.getCode(), "邮箱格式错误");
        }

        BaseResponse<LoginResponse> response = new BaseResponse<>();

        try {
            if (StringUtils.isEmpty(request.getContact()) || StringUtils.isEmpty(request.getPassword())) {
                return BaseResponse.fail(DigitalResultEnum.PARAM_ERROR);
            }
            String aesKey = request.getAesKey().substring(0, 16);
            // AES解密出RSA私钥
            String privateKey = decryptPrivateKey(request.getPrivateContent(), aesKey);
            // RSA解密
            Map<String, String> codeMap = decryptPublicKey(request.getPassword(), privateKey);
            // 明文
            String plainPwd = codeMap.get(PASSWORD);
            String publicKey = codeMap.get(PUBLIC_KEY);
            logger.info("login.plainPwd:{}, publicKey:{}", plainPwd, publicKey);
            // 本地加密与数据库对比
            String localEncPwd = SensitiveInfoUtil.encrypt(plainPwd, sensitiveCipherKey);
            logger.info("codeMap: {}", JSONObject.toJSONString(codeMap));

            // 获取短信验证码 进行验证
            InnerGetVerifyCodeReq verifyCodeReq = new InnerGetVerifyCodeReq();
            verifyCodeReq.setContact(request.getContact());
            verifyCodeReq.setType(request.getType());
            verifyCodeReq.setVerifyCode(request.getVerifyCode());

            BaseResponse<GetVerifyCodeResp> miscResp = miscRemoteService.getVerifyCode(verifyCodeReq);
            if (DigitalResultEnum.SUCCESS.getCode() != miscResp.getCode()){
                logger.info("login.miscResp.resp:{}", JSON.toJSONString(miscResp));
                return BaseResponse.fail(miscResp.getCode(),miscResp.getMessage());
            }
            // 5. 查询用户（合并查询逻辑）
            LambdaQueryWrapper<UserInfoDo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserInfoDo:: getStatus,UserStatusEnum.NORMAL.getType());
            if (typeEnum == VerifyCodeTypeEnum.EMAIL) {
                queryWrapper.eq(UserInfoDo::getEmail, request.getContact());
            } else {
                queryWrapper.eq(UserInfoDo::getTelephone, request.getContact());
            }
            UserInfoDo userInfoDo = userInfoRepository.selectOne(queryWrapper);
            UserInfoDo user;

            // 6. 注册/登录逻辑
            if (userInfoDo == null) {
                // 注册（独立事务方法）
                user = registerUser(request.getContact(), localEncPwd, request.getType());
            } else {
                // 校验用户状态
                if (UserStatusEnum.FREEZE.getType().equals(userInfoDo.getStatus())) {
                    return BaseResponse.fail(UserResultEnum.USER_FROZEN.getCode(), UserResultEnum.USER_FROZEN.getMsg());
                }
                if (UserStatusEnum.DELETED.getType().equals(userInfoDo.getStatus())) {
                    return BaseResponse.fail(UserResultEnum.USER_DELETE.getCode(), UserResultEnum.USER_DELETE.getMsg());
                }

                // 校验密码
                if (!plainPwd.equals(userInfoDo.getPassword())) {
                    return BaseResponse.fail(UserResultEnum.BAD_COMBINATION.getCode(),UserResultEnum.BAD_COMBINATION.getMsg());
                }
                user = userInfoDo;
            }

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(user.getId());
            loginResponse.setUsername(user.getName());
            // 缓存token
            String token = getToken(user.getId(), user.getName(), publicKey);
            // 缓存refresh token
            String refreshToken = generateRefreshToken(user.getId(), user.getName(), publicKey);
            loginResponse.setToken(token);
            // 缓存用户信息
            cacheUserInfo(user);
            loginResponse.setRefreshToken(refreshToken);
            response.setData(loginResponse);
            //放token到cookie
//            httpServletResponse.addCookie(createCookie(Constants.TOKEN_NAME, token, -1, domain, true));
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


    @Override
    public void logout(BaseRequest request) {
        logout(request.getUserId().toString(), request.getDeviceId());
//        httpServletResponse.addCookie(createCookie(Constants.TOKEN_NAME, null, 0, "", false));
    }

    @Override
    public BaseResponse<UserInfoResp> getUserInfo(IncludePkRequest req) {
        logger.info("getUserInfo.req:{}",JSON.toJSONString(req));
        BaseResponse<UserInfoResp> userBaseResponse = new BaseResponse();

        UserInfoResp userInfo = new UserInfoResp();
        UserInfoDo userInfoDO = userInfoRepository.selectById(req.getUserId());
        if (null != userInfoDO){
            userInfo.setUserId(userInfoDO.getId());
            userInfo.setUsername(userInfoDO.getName());
            userInfo.setEmail(userInfoDO.getEmail());
            userInfo.setTelephone(userInfoDO.getTelephone());
            userInfo.setLightning(userInfoDO.getLightning());
            userInfo.setStatus(userInfoDO.getStatus());
            userInfo.setIconUrl(userInfoDO.getIconUrl());
            userInfo.setUserType(userInfoDO.getUserType());
            userInfo.setCreateTime(userInfoDO.getCreateTime());
        }

        userBaseResponse.setData(userInfo);
        userBaseResponse.setCode(UserResultEnum.SUCCESS.getCode());
        userBaseResponse.setMessage(UserResultEnum.SUCCESS.getMsg());
        return userBaseResponse;
    }

    public void logout(String userId, String oldDeviceId){
        if (StringUtils.isEmpty(oldDeviceId) && StringUtils.isEmpty(userId)) {
            throw new LifpayException(UserResultEnum.BAD_INPUT);
        }
        removeToken(userId, oldDeviceId);
        removeRefreshToken(userId, oldDeviceId);
    }

    // 独立的注册事务方法
    @Transactional(rollbackFor = Exception.class)
    private UserInfoDo registerUser(String contact, String encryptPwd, Integer type) {
        UserInfoDo userInfoDo = new UserInfoDo();
        VerifyCodeTypeEnum typeEnum = VerifyCodeTypeEnum.getByType(type);
        if (typeEnum == VerifyCodeTypeEnum.EMAIL) {
            userInfoDo.setEmail(contact);
            // 随机用户名（示例：user_手机号后4位/邮箱前缀）
            userInfoDo.setName(RegularExpressionUtil.extractEmailPrefix(contact));
        } else {
            userInfoDo.setTelephone(contact);
            userInfoDo.setName(RegularExpressionUtil.extractMobileLast4(contact));
        }

        userInfoDo.setPassword(encryptPwd);
        userInfoDo.setUserType(UserTypeEnum.PERSON.getType());
        userInfoDo.setStatus(UserStatusEnum.NORMAL.getType());
        userInfoDo.setCreateTime(System.currentTimeMillis());
        userInfoDo.setUpdateTime(userInfoDo.getCreateTime());
        userInfoRepository.insert(userInfoDo);

        return userInfoDo;
    }


    @Override
    public BaseResponse updateUserInfo(UpdateUserInfoReq req) {
        logger.info("updateUserInfo.req:{}",JSON.toJSONString(req));
        if (StringUtils.isNotEmpty(req.getBio()) && req.getBio().length() > 120){
            return BaseResponse.fail(UserResultEnum.BAD_INPUT.getCode(),UserResultEnum.BAD_INPUT.getMsg());
        }
        BaseResponse response = new BaseResponse<>();
        // 根据用户id 查询用户信息
        UserInfoDo userInfoDO = userInfoRepository.selectById(req.getUserId());
        if (null == userInfoDO){
            return BaseResponse.fail(UserResultEnum.USER_NOT_EXISTS.getCode(),UserResultEnum.USER_NOT_EXISTS.getMsg());
        }
        userInfoDO.setName(req.getNickName());
        userInfoDO.setBio(req.getBio());
        userInfoDO.setIconUrl(req.getIconUrl());
        userInfoDO.setUpdateTime(System.currentTimeMillis());

        userInfoRepository.updateById(userInfoDO);
        logger.info("updateUserInfo.done:{}",JSON.toJSONString(userInfoDO));
        response.setCode(UserResultEnum.SUCCESS.getCode());
        response.setMessage(UserResultEnum.SUCCESS.getMsg());
        return response;
    }

    @Override
    public BaseResponse updateLightningAddress(UserLightningAddressReq req) {
        logger.info("updateLightningAddress.req:{}",JSON.toJSONString(req));
        BaseResponse response = new BaseResponse<>();

        // 根据用户id 查询用户信息
        UserInfoDo userInfoDO = userInfoRepository.selectById(req.getUserId());
        if (null == userInfoDO){
            return BaseResponse.fail(UserResultEnum.USER_NOT_EXISTS.getCode(),UserResultEnum.USER_NOT_EXISTS.getMsg());
        }
        userInfoDO.setLightning(req.getLightningAddress());
        // 更新用户的Lightning地址
        userInfoRepository.updateById(userInfoDO);


        response.setCode(UserResultEnum.SUCCESS.getCode());
        response.setMessage(UserResultEnum.SUCCESS.getMsg());
        return response;
    }

    @Override
    public BaseResponse<RefreshTokenResDto> checkRefreshToken(RefreshTokenReq req) {
        BaseResponse<RefreshTokenResDto> response = new BaseResponse<>();
        RefreshTokenResDto refreshTokenResDto = new RefreshTokenResDto();
        String privateKey = decryptPrivateKey(req.getPrivateContent(), req.getAesKey().substring(0, 16));
        Map<String, String> codeMap = decryptPublicKey(req.getRefreshToken(), privateKey);
        logger.info("codeMap: {}", codeMap);
        String reqRefreshToken = codeMap.get(PASSWORD);
        String publicKey = codeMap.get(PUBLIC_KEY);
        refreshTokenResDto.setPublicKey(publicKey);
        // 根据refresh token 获取用户ID 看是否能获取的到
        String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_REFRESH_TOKEN, reqRefreshToken);
        String jsonToken = redisDS.getStr(tokenKey);
        req.setDeviceId(getUserDeviceIdFrmPubKey(publicKey));

        if (jsonToken == null) {
            response.setCode(UserResultEnum.INVALID_REFRESH_TOKEN.getCode());
            response.setMessage(UserResultEnum.INVALID_REFRESH_TOKEN.getMsg());
            return response;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonToken);
        String userId = jsonObject.getString("userId");
        log.info("checkRefreshToken 根据refresh token 获取到用户id: {}", userId);
        // 1。根据用户ID 获取redis 中的 refresh token
        String refreshToken = getRefreshToken(Long.parseLong(userId), req.getDeviceId());
        log.info("根据用户ID和设备号获取到用户的 refreshToken: {}", refreshToken);
        // 判断是否为空
        if (StringUtils.isBlank(refreshToken)) {
            log.info("refreshToken 为空");
            // refresh token 失效用户重新登陆
            response.setCode(UserResultEnum.INVALID_REFRESH_TOKEN.getCode());
            response.setMessage(UserResultEnum.INVALID_REFRESH_TOKEN.getMsg());
            return response;
        }
        if (refreshToken.equals(reqRefreshToken)) {
            // 延长refresh token 的有效时间
            extensionRefreshToken(Long.parseLong(userId), req.getDeviceId(), refreshToken);
            log.info("有效的refreshToken 延长refreshToken有效期");
            // refresh token 是有效的 查看 token 是否是有效的
            String token = getTokenValue(Long.parseLong(userId), getUserDeviceIdFrmPubKey(publicKey));
            // 如果token 无效 则生成新的token 给到客户端
            LambdaQueryWrapper<UserInfoDo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserInfoDo:: getStatus,UserStatusEnum.NORMAL.getType());
            queryWrapper.eq(UserInfoDo:: getId, userId);
            UserInfoDo userInfoDo = userInfoRepository.selectOne(queryWrapper);
//            AdminUserDO adminUserDO = adminUserDao.getById(userId);
            if (StringUtils.isBlank(token)) {
                log.info("用户token 已失效，生成新的token给到前端");
                token = getToken(Long.parseLong(userId), userInfoDo.getName(), publicKey);
            } else {
                log.info("在刷新refresh token 的时候，检测到token 依然还是有效的延长token并且返回老的token 给前端");
                extensionUserToken(Long.parseLong(userId), userInfoDo.getName(), req.getDeviceId(), token, publicKey);
                log.info("extensionUserToken userId: {}, deviceID:{}, token:{}", userId, req.getDeviceId(), token);
            }
            // 更新用户信息缓存
            cacheUserInfo(userInfoDo);
            //放token到cookie
//            httpServletResponse.addCookie(createCookie(Constants.TOKEN_NAME, token, -1, domain, true));
            // 返回原有的refresh token 给到前端
            refreshTokenResDto.setRefreshToken(refreshToken);
            refreshTokenResDto.setToken(token);
            response.setData(refreshTokenResDto);
            response.setCode(UserResultEnum.SUCCESS.getCode());
            response.setMessage(UserResultEnum.SUCCESS.getMsg());
        } else {
            log.info("reqRefreshToken not equal to redis refreshToken");
            response.setCode(UserResultEnum.INVALID_REFRESH_TOKEN.getCode());
            response.setMessage(UserResultEnum.INVALID_REFRESH_TOKEN.getMsg());
        }
        return response;
    }

    private void cacheUserInfo(UserInfoDo userInfoDo) {
        // 从数据库获取用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userInfoDo.getId());
        userInfo.setUserName(userInfoDo.getName());

//        AdminUserInfo adminUserInfo = adminUserDao.getAdminUserInfoById(userInfoDo.getId());
        AuthUtil.setUserInfoCache(userInfo, tokenValidTime);
    }

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
    private String generateRefreshToken(long userId, String deviceId, String publicKey) {
        String key = getRefreshTokenKey(String.valueOf(userId), getUserDeviceIdFrmPubKey(publicKey));
        String refreshToken = createToken();
        logger.info("start generateRefreshToken:{}", refreshToken);
        //设置refresh token 有效时间
        redisDS.setex(key, refreshToken, refreshTokenValidTime);
        String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_REFRESH_TOKEN, refreshToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("publicKey", publicKey);
        redisDS.setex(tokenKey, jsonObject.toJSONString(), refreshTokenValidTime);
        logger.info("end generateRefreshToken:{}", jsonObject.toJSONString());
        return refreshToken;
    }

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

    private static Map<String, String> decryptPublicKey(String reqCode, String privateKey) {
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
    private static String decryptPrivateKey(String privateContent, String aesKey) {
        String privateStr = AESCBCUtils.decrypt(privateContent, aesKey);
        String privateKey = privateStr.split("&&")[0];
        if (privateKey == null) {
            log.error("decryptPrivateKey fail {}", privateContent);
            throw new LifpayException(UserResultEnum.BAD_COMBINATION.getCode(), UserResultEnum.BAD_COMBINATION.getMsg());
        }
        return privateKey;
    }


    protected long removeToken(String userId, String deviceId) {
        log.info("start to remove token from Redis, userId={}, deviceId={}", userId, deviceId);
        String key = getTokenKey(userId, deviceId);
        String token = redisDS.getStr(key);
        long rs = 0;
        if (null != token) {
            String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_TOKEN, token);
            rs = redisDS.del(key);
            rs += redisDS.del(tokenKey);
        }
        log.info("user {} token removed {}", key, token);
        return rs;
    }

    protected long removeRefreshToken(String userId, String deviceId) {
        log.info("start to remove refresh token from Redis, userId={}, deviceId={}", userId, deviceId);
        String key = getRefreshTokenKey(userId, deviceId);
        String token = redisDS.getStr(key);
        long rs = 0;
        if (null != token) {
            String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_REFRESH_TOKEN, token);
            rs = redisDS.del(key);
            rs += redisDS.del(tokenKey);
        }
        log.info("user {} refresh token removed {}", key, token);
        return rs;
    }



    /**
     * //从redis获取refresh_token
     *
     * @param userId 用户id
     * @return refresh_token
     */
    private String getRefreshToken(long userId, String deviceId) {
        String key = getRefreshTokenKey(String.valueOf(userId), deviceId);
        return redisDS.getStr(key);
    }

    /**
     * //从redis获取token
     *
     * @param userId 用户id
     * @return token
     */
    private String getTokenValue(long userId, String deviceId) {
        String key = getTokenKey(String.valueOf(userId), deviceId);
        return redisDS.getStr(key);
    }

    /**
     * //延长refresh token有效期
     *
     * @param userId 用户id deviceId 设备号
     *               value app 发送过来的refresh token
     */
    private void extensionRefreshToken(long userId, String deviceId, String value) {
        String key = getRefreshTokenKey(String.valueOf(userId), deviceId);
        redisDS.setex(key, value, refreshTokenValidTime);
        String refreshTokenKey = String.format(RedisDBKey.GET_USER_ID_BY_REFRESH_TOKEN, value);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("timestamp", System.currentTimeMillis());
        redisDS.setex(refreshTokenKey, jsonObject.toJSONString(), refreshTokenValidTime);
    }

    /**
     * //延长用户 token有效期
     *
     * @param userId 用户id deviceId 设备号
     *               value app 发送过来的refresh token
     */
    private void extensionUserToken(long userId, String userName, String deviceId, String value, String publicKey) {
        String key = getTokenKey(String.valueOf(userId), deviceId);
        redisDS.setex(key, value, tokenValidTime);
        log.info("extensionUserToken: {}", value);
        String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_TOKEN, value);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("publicKey", publicKey);
        redisDS.setex(tokenKey, jsonObject.toJSONString(), tokenValidTime);
        String userNameKey = getUserNameTokenKey(String.valueOf(userId));
        redisDS.setex(userNameKey, userName, tokenValidTime);
    }

    private String getUserDeviceIdFrmPubKey(String publicKey) {
        if (publicKey.startsWith(VALUE)) {
            return publicKey.substring(0, 22);
        }
        return publicKey.substring(0, 20);
    }
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
            String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_TOKEN, token);
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


    public static void main(String[] args) throws Exception {
//        testLoginEncryptDecrypt();
    }

//    public static void testLoginEncryptDecrypt() throws Exception {
//        // ========= 1. 模拟前端准备数据 =========
//        String clientPublicKey = ""; // 前端自己生成的椭圆曲线的公钥
//        String plainPassword = "123456"; // 明文密码
//
//        // 关键：按后端预期拆分数据为两部分（这里简单按长度拆分，实际前端需保持一致）
//        int splitIndex = plainPassword.length() / 2;
//        String passwordPart1 = plainPassword.substring(0, splitIndex); // 密码前半段
//        String passwordPart2 = plainPassword.substring(splitIndex);     // 密码后半段
//
//        // part1明文 =rsa公钥 + "&" + 密码前半段（后端解密后需要提取publicKey）
//        String part1Plain = clientPublicKey + "&" + passwordPart1;
//        // part2明文 = 密码后半段
//        String part2Plain = passwordPart2;
//
//        // ========= 2. 前端用RSA公钥加密两部分 =========
//        String part1Enc = RSASignature.doEncrypt(part1Plain, RSA_PUBLIC_KEY); // 加密part1
//        String part2Enc = RSASignature.doEncrypt(part2Plain, RSA_PUBLIC_KEY); // 加密part2
//
//        // 前端传入的password = 两部分密文用&拼接（核心修正点）
//        String reqPasswordtt = part1Enc + "&" + part2Enc;
//        logger.info(reqPasswordtt);
//
//        String reqPassword = "KsBcvg1iLhsBUWR15WwukBTHhzcA2kpQMKqNBxgYyocg0kbDJbGtSha6BXR6bReU2PZDLa6ItePz2UONqn1E/+GkNx6660E4dSePZx+h+wrmdJZ27ZtYOpgGrn6MqrnhV7ScP4IeGQN/sCA2lS883Zde5PBAHIzfOnfcuzfV9rmHtH7fE4NsSM2rLMO8U6YlNtalXYQJ2j4wjlotPPQI/CAxeRWFJdWmKpnXkZ9zQh4j1w6sbTQrw7cCx2A6egZN5wFPy5wHKBBDUOMz0FHffCamzFIERwmY5vrSAtjfwVyszfSXMazJFxyHcmJbZes2inUY7ostDrmqfPCUXvMymg==&eI8E3EsVPEJTKHjsbh1T5utdUfJu5yDqglooPxiXkw8kibtYEzBy4eEsS6MkuS/0cZPrwbYlckpy4Wk3htiufz+Z8BDXei9Dos7qALyT2SnG1PGk2cf6nvsdv7KDt8ta+QGR5otnxF9PVxA95aXSLZjB9Faqpq9+tKU9vfslFpLhrOz8ZUizkXBUveMOUOhyx68GHZQY7so3dGfemAZRDPILUgSVauKN0tmRK/xqa5JDtyEEY0ljbGzKIptT12Be3bal2YNN9VffliVXY2vZL8ZMO72fuWgIM8eBDrVp9xmKNUu+TFeOERMTTsUDKwiHphoRKk05QonjQeGSq5dsDQ==";
//
//        // ========= 3. 模拟网关生成privateContent =========
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String privateContentStr = RSA_PRIVATE_KEY + "&&" + timestamp; // 私钥+时间戳
//        String privateContent = AESCBCUtils.encrypt(privateContentStr, AES_KEY.substring(0, 16)); // AES加密
//
//        // ========= 4. 构造LoginRequest =========
//        LoginRequest req = new LoginRequest();
////        req.setUsername(RSASignature.doEncrypt("testUser", RSA_PUBLIC_KEY)); // 用户名加密（示例）
//        req.setPassword(reqPassword); // 关键：使用&拼接的密文
//        req.setAesKey(AES_KEY);
//        req.setPrivateContent(privateContent);
//
//        // ========= 5. 后端解密流程（复用现有代码） =========
//        String aesKey = req.getAesKey().substring(0, 16);
//        String privateKey = decryptPrivateKey(req.getPrivateContent(), aesKey); // 解密得到RSA私钥
//        Map<String, String> codeMap = decryptPublicKey(req.getPassword(), privateKey); // 现在split不会报错
//
//        // ========= 6. 验证结果 =========
//        System.out.println("解密得到的明文密码: " + codeMap.get(PASSWORD)); // 应输出123456
//        System.out.println("解密得到的客户端公钥: " + codeMap.get(PUBLIC_KEY)); // 应输出client_public_key_xxx
//    }


}
