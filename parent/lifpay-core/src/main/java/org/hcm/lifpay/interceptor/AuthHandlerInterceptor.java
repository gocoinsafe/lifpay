package org.hcm.lifpay.interceptor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.annotation.RequiresPermission;
import org.hcm.lifpay.annotation.RequiresUserType;
import org.hcm.lifpay.context.SecurityContextHolder;
import org.hcm.lifpay.dto.UserInfo;
import org.hcm.lifpay.util.AuthUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet拦截器，处理权限检查
 *
 * @author xinzhe
 */
@Slf4j
public class AuthHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (handlerMethod.getMethodAnnotation(RequiresUserType.class) == null && handlerMethod.getMethodAnnotation(RequiresPermission.class) == null) {
                    return true;
                }
                BodyReaderWrapper bodyReaderWrapper = new BodyReaderWrapper(request);
                byte[] bodyBytes = StreamUtils.copyToByteArray(bodyReaderWrapper.getInputStream());
                String body = new String(bodyBytes, request.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject != null) {
                    Long userId = jsonObject.getLong("userId");
                    if (userId != null) {
                        // 从redis缓存获取
                        UserInfo userInfo = AuthUtil.getUserInfoCache(userId);
                        // 存到THREAD LOCAL
                        AuthUtil.setUserInfoLocal(userInfo);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        SecurityContextHolder.remove();
    }
}

