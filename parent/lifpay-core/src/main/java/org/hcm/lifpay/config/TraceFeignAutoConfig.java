package org.hcm.lifpay.config;

import org.hcm.lifpay.interceptor.TraceFeignRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务间feign调用日志链路配置
 * @author xinzhe
 */
@Configuration
@ConditionalOnClass(name = {"feign.template.Template"})
public class TraceFeignAutoConfig {
    @Bean
    @ConditionalOnClass(name = "feign.template.Template")
    public TraceFeignRequestInterceptor traceFeignRequestInterceptor() {
        return new TraceFeignRequestInterceptor();
    }
}