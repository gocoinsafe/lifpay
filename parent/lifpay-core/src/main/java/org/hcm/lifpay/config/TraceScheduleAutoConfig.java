package org.hcm.lifpay.config;

import org.hcm.lifpay.interceptor.ScheduleInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring定时任务拦截器配置
 * @author xinzhe
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.scheduling.SchedulingTaskExecutor"})
public class TraceScheduleAutoConfig {
    @Bean
    @ConditionalOnClass(name = "org.springframework.scheduling.SchedulingTaskExecutor")
    public ScheduleInterceptor scheduleInterceptor() {
        return new ScheduleInterceptor();
    }
}
