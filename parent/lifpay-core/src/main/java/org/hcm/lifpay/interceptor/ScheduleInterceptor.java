package org.hcm.lifpay.interceptor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hcm.lifpay.common.Constants;
import org.hcm.lifpay.util.TraceUtil;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * spring定时任务拦截器，spring定时任务的日志链路入口
 * @author xinzhe
 */
@Aspect
@Order(-1)
@Data
@Slf4j
public class ScheduleInterceptor {
    // 拦截@Scheduled注解的方法
    @Around("@annotation(scheduled)")
    public Object handle(ProceedingJoinPoint point, Scheduled scheduled) throws Throwable {
        try {
            // 生成新的链路Id，并添加到MDC
            String traceId = TraceUtil.genTraceId();
            MDC.put(Constants.MDC_KEY_TRACE_ID, traceId);
            log.debug("======================new traceId: {}=============================", traceId);
            // 执行方法
            return point.proceed();
        } finally {
            log.debug("====================== remove traceId: {} =============================", MDC.get(Constants.MDC_KEY_TRACE_ID));
            // 方法执行后始终从MDC移除
            MDC.remove(Constants.MDC_KEY_TRACE_ID);
        }
    }


}

