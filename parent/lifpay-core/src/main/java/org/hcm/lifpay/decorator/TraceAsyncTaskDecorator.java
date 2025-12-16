package org.hcm.lifpay.decorator;

import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.Constants;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * Task装饰器，处理使用线程池的异步处理，主线程到子线程的日志链路id传递
 * @author xinzhe
 */
@Slf4j
public class TraceAsyncTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取当前线程的上下文map
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    log.debug("======================trance: {}=============================", contextMap.get(Constants.MDC_KEY_TRACE_ID));
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } catch (Exception exception) {
                log.warn(exception.getMessage());
            } finally {
                log.debug("====================== remove traceId: {} =============================", MDC.get(Constants.MDC_KEY_TRACE_ID));
                // 从MDC移除
                MDC.clear();
            }
        };
    }
}
