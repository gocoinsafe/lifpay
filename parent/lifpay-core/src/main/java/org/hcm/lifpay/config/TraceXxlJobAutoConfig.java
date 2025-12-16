package org.hcm.lifpay.config;

import org.hcm.lifpay.interceptor.XxlJobInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxljob拦截器配置
 * @author xinzhe
 */
@Configuration
@ConditionalOnClass(name = {"com.xxl.job.core.executor.XxlJobExecutor"})
public class TraceXxlJobAutoConfig {
    @Bean
    @ConditionalOnClass(name = "com.xxl.job.core.executor.XxlJobExecutor")
    public XxlJobInterceptor xxlJobInterceptor() {
        return new XxlJobInterceptor();
    }
}