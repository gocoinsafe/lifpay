package org.hcm.lifpay.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.Constants;
import org.hcm.lifpay.util.TraceUtil;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet拦截器，处理controller日志链路
 * @author xinzhe
 */
@Slf4j
public class TraceContollerHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从header获取
        String traceId = request.getHeader(Constants.MDC_KEY_TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            // 如果没有就新建
            traceId = TraceUtil.genTraceId();
            log.debug("======================new traceId: {}=============================", traceId);
        } else {
            log.debug("======================trance: {}=============================", traceId);
        }
        // 放到MDC
        MDC.put(Constants.MDC_KEY_TRACE_ID, traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
        try {
            log.debug("====================== remove traceId: {} =============================", MDC.get(Constants.MDC_KEY_TRACE_ID));
            // 请求完成后从MDC移除
            MDC.remove(Constants.MDC_KEY_TRACE_ID);
        } catch (Exception exception) {
        }
    }
}
