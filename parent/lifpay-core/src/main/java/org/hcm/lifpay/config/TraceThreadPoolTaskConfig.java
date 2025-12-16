package org.hcm.lifpay.config;

import lombok.Data;
import org.hcm.lifpay.decorator.TraceAsyncTaskDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * ThreadPoolTaskExecutor线程池，主线程到子线程的链路处理
 * @author xinzhe
 */
@Configuration
@Data
public class TraceThreadPoolTaskConfig {
    @Autowired(required = false)
    void setThreadPoolTaskExecutorTaskDecorator(@Autowired List<ThreadPoolTaskExecutor> threadPoolTaskExecutors) {
        // 为所有的ThreadPoolTaskExecutor都加上链路处理的装饰器
        if (!CollectionUtils.isEmpty(threadPoolTaskExecutors)) {
            threadPoolTaskExecutors.forEach(p->{
                // 设置装饰器
                p.setTaskDecorator(new TraceAsyncTaskDecorator());
                // 让装饰器生效
                p.initialize();
            });
        }
    }
}
