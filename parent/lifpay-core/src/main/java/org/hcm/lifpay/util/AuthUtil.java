//package org.hcm.lifpay.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.hcm.lifpay.annotation.RequiresPermission;
//import org.hcm.lifpay.annotation.RequiresUserType;
//import org.hcm.lifpay.common.enums.AdminUserTypeEnum;
//import org.hcm.lifpay.config.AuthConfig;
//import org.hcm.lifpay.exception.NoAccessUserTypeException;
//import org.hcm.lifpay.exception.NotLoginException;
//import org.hcm.lifpay.redis.RedisDBKey;
//import org.hcm.lifpay.redis.RedisDS;
//
//
//import java.util.List;
//
///**
// * Token 权限验证工具类
// *
// * @author ruoyi
// */
//
//public class AuthUtil {
//    public static RedisDS redisDs = SpringUtils.getBean(RedisDS.class);
//    public static AuthConfig authConfig = SpringUtils.getBean(AuthConfig.class);
//
//    /**
//     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
//     *
//     * @param requiresUserType 权限注解
//     */
//    public static void checkUserType(RequiresUserType requiresUserType) {
//        AdminUserTypeEnum[] checkUserTypes = requiresUserType.value();
//        // 从local获取用户信息
//        AdminUserInfo adminUserInfo = getUserInfoLocal();
//        // 正常是从网关过来的，都会有用户信息，如果没有，可能是swagger直接调的
//        if (adminUserInfo == null) {
//            // 如果开启了免登录的配置，跳过
//            if (authConfig.isNoAuth()){
//                return;
//            }
//            throw new NotLoginException();
//        }
//        for (AdminUserTypeEnum checkUserType : checkUserTypes) {
//            if (checkUserType.equals(adminUserInfo.getUserType())) {
//                return;
//            }
//        }
//        throw new NoAccessUserTypeException();
//    }
//
//    /**
//     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
//     *
//     * @param requiresPermission 权限注解
//     */
//    public static void checkPermission(RequiresPermission requiresPermission) {
//        String checkUserPermission = requiresPermission.value();
//        // 从local获取用户信息
//        AdminUserInfo adminUserInfo = getUserInfoLocal();
//        // 正常是从网关过来的，都会有用户信息，如果没有，可能是swagger直接调的
//        if (adminUserInfo == null) {
//            // 如果开启了免登录的配置，跳过
//            if (authConfig.isNoAuth()){
//                return;
//            }
//            throw new NotLoginException();
//        }
//        // 超级管理员有权限，其他类型的账户需要看是否配置了权限
//        if (AdminUserTypeEnum.ADMIN.getCode() != adminUserInfo.getUserType().getCode()) {
//            boolean userHashPermission = hashPermission(checkUserPermission, adminUserInfo.getUserPermission());
//            if (!userHashPermission) {
//                throw new NoPermissionException();
//            }
//        }
//    }
//
//    private static boolean hashPermission(String checkPermission, List<String> userPermissionList) {
//        String permission = userPermissionList.stream().filter(p -> p.equals(checkPermission)).findFirst().orElse(null);
//        return permission != null;
//    }
//
//    public static AdminUserInfo getUserInfoLocal() {
//        return SecurityContextHolder.get(Constants.THREAD_LOCAL_KEY_CURRENT_USER, AdminUserInfo.class);
//    }
//
//    public static void setUserInfoLocal(AdminUserInfo userInfo) {
//        SecurityContextHolder.set(Constants.THREAD_LOCAL_KEY_CURRENT_USER, JSON.toJSONString(userInfo));
//    }
//
//
//    public static AdminUserInfo getUserInfoCache(Long userId) {
//        // 从redis获取
//        String key = RedisDBKey.getAdminUserInfoKey(userId);
//        String userInfo = redisDs.getStr(key);
//        return JSON.parseObject(userInfo, AdminUserInfo.class);
//    }
//
//    public static void setUserInfoCache(AdminUserInfo userInfo, int expire) {
//        String key = RedisDBKey.getAdminUserInfoKey(userInfo.getUserId());
//        String userInfoStr = JSONObject.toJSONString(userInfo);
//        redisDs.setex(key, userInfoStr, expire);
//    }
//}
