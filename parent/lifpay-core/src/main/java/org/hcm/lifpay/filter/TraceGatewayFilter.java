//package org.hcm.lifpay.filter;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.hcm.lifpay.common.Constants;
//import org.hcm.lifpay.util.TraceUtil;
//import org.slf4j.MDC;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import reactor.util.context.Context;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//
//import java.util.List;
//
///**
// * 网关链路过滤器，处理网关的日志链路
// *
// * @author xinzhe
// */
//@Slf4j
//@Data
//@ConditionalOnClass(name = "org.springframework.cloud.gateway.filter.GlobalFilter")
//public class TraceGatewayFilter implements GlobalFilter, Ordered {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 从请求头获取
//        String traceId = getTraceIdFromHeader(exchange.getRequest().getHeaders());
//        // 如果没有，就新建
//        if(StringUtils.isEmpty(traceId)){
//            traceId = TraceUtil.genTraceId();
//            log.debug("======================new traceId: {}=============================", traceId);
//        }else{
//            log.debug("======================trance: {}=============================", traceId);
//        }
//        // 链路id放到MDC
//        MDC.put(Constants.MDC_KEY_TRACE_ID, traceId);
//        // 将链路id放到header
//        String finalTraceId = traceId;
//        ServerHttpRequest newExchange = exchange.getRequest().mutate().headers(httpHeaders ->
//                httpHeaders.add(Constants.MDC_KEY_TRACE_ID, finalTraceId)).build();
//        // 往下执行，在终止时从MDC移除链路id
//        return chain.filter(exchange.mutate().request(newExchange).build())
//                .subscriberContext(Context.of(Constants.MDC_KEY_TRACE_ID, MDC.get(Constants.MDC_KEY_TRACE_ID)))
//                .doFinally(s -> {
//                    log.debug("====================== remove traceId: {} =============================", MDC.get(Constants.MDC_KEY_TRACE_ID));
//                    MDC.remove(Constants.MDC_KEY_TRACE_ID);
//                });
//    }
//
//    private String getTraceIdFromHeader(HttpHeaders headers){
//        // 从请求头获取
//        List<String> traceList = headers.get(Constants.MDC_KEY_TRACE_ID);
//        String traceId = null;
//        if (!CollectionUtils.isEmpty(traceList)) {
//            // 如果请求头有链路id，从请求头获取
//            traceId = traceList.get(0);
//        }
//        return traceId;
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE + 1;
//    }
//}
