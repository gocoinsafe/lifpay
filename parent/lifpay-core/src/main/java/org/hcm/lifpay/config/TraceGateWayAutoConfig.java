//package org.hcm.lifpay.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Hooks;
//import reactor.core.publisher.Operators;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
///**
// * 网关链路处理配置
// * @author xinzhe
// */
//@Slf4j
//@Configuration
//@ConditionalOnClass(name = {"org.springframework.cloud.gateway.filter.GlobalFilter"})
//public class TraceGateWayAutoConfig {
//    private static final String MDC_CONTEXT_REACTOR_KEY = TraceGateWayAutoConfig.class.getName();
//    @Bean
//    public TraceGatewayFilter traceGlobalFilter() {
//        return new TraceGatewayFilter();
//    }
//
//    @PostConstruct
//    public void contextOperatorHook() {
//        Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY,
//                Operators.lift((scannable, coreSubscriber) -> new TraceReactorSubscriber<>(coreSubscriber)));
//    }
//
//    @PreDestroy
//    public void cleanupHook() {
//        Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY);
//    }
//
//
//}