package org.hcm.lifpay.redis;



/**
 * @author xinzhe
 */
public class RedisDBKey {

    /**
     * 根据UserId&driveId 查token
     */
    public static final String GET_TOKEN_BY_USER = "user:id:%s:driveId:%s:token";


    /**
     * 根据UserId 查token
     */
    public static final String GET_USERNAME_BY_USERID = "userName:id:%s:username";

    /**
     * 根据UserId&driveId 查refresh token
     */
    public static final String GET_REFRESH_TOKEN_BY_USER = "user:id:%s:driveId:%s:refresh_token";


    public static final String REQUEST_CHECK = "keyGateway:requestId:%s";

    /**
     * 根据token查userId
     */
    public static final String GET_USER_ID_BY_TOKEN = "user:token:%s";


    /**
     * 根据refresh token查userId
     */
    public static final String GET_USER_ID_BY_REFRESH_TOKEN = "user:refresh_token:%s";

    /**
     * 根据adminUserId查用户信息
     */
    public static final String USER_INFO = "user:id:%s:userinfo";

    /**
     * login:webDeviceId:type:uuid
     */
    public static final String LOGIN_QR_CODE_KEY = "login:%s:%s:%s";


    public static final String GET_TOKEN_BY_USERID = "dashboard:token:%s";

    public static String getAdminUserInfoKey(Long userId){
        return String.format(RedisDBKey.USER_INFO, userId);
    }

}
