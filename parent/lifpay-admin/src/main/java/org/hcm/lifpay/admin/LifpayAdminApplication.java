package org.hcm.lifpay.admin;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@MapperScan("org.hcm.lifpay.admin.dao.mapper")
@Slf4j
public class LifpayAdminApplication {

//    @Autowired
//    protected RedisDSConfiguration redisConfiguration;
//
//    @Bean
//    public RedisDS createRedis() {
//        return RedisDS.create(redisConfiguration);
//    }

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(LifpayAdminApplication.class, args);
        log.info("=======================Admin Application is run ....========================");
    }
}
