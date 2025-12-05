package org.hcm.lifpay.user;


import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.config.RedisDSConfiguration;
import org.hcm.lifpay.redis.RedisDS;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@MapperScan("org.hcm.lifpay.user.dao.repository")
@Slf4j
public class UserApplication {


    @Autowired
    protected RedisDSConfiguration redisConfiguration;

    @Bean
    public RedisDS createRedis() {
        return RedisDS.create(redisConfiguration);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        log.info("=======================user Application is run ....========================");
    }

}
