package org.hcm.lifpay.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
@ConditionalOnProperty(name = "spring.redis.host")
public class RedisDSConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.timeout:2000}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle:2000}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait:2000}")
    private long maxWaitMillis;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;



}
