package org.hcm.lifpay.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.Constants;
import org.hcm.lifpay.util.TraceUtil;
import org.slf4j.MDC;

/**
 * feign请求拦截器，处理服务间feign调用链路id传递
 * @author xinzhe
 */
@Slf4j
public class TraceFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从MDC获取
        String traceId = MDC.get(Constants.MDC_KEY_TRACE_ID);
        try {
            // 如果没有就新建
            if (StringUtils.isEmpty(traceId)) {
                traceId = TraceUtil.genTraceId();
                log.debug("======================new traceId: {}=============================", traceId);
            }else{
                log.debug("======================trance: {}=============================", traceId);
            }
        } finally {
            // 放到header中
            template.header(Constants.MDC_KEY_TRACE_ID, traceId);
        }
    }
}