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




}
