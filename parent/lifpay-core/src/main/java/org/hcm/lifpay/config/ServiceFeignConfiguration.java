package org.hcm.lifpay.config;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author xinzhe
 */
public class ServiceFeignConfiguration {
    @Value("${service.feign.connectTimeout:60000}")
    private int connectTimeout;

    @Value("${service.feign.readTimeOut:60000}")
    private int readTimeout;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }

}
