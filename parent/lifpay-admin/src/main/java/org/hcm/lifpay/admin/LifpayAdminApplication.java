package org.hcm.lifpay.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
//@MapperScan("org.hcm.lifpay.admin.dao.mapper")
@Slf4j
public class LifpayAdminApplication {

//    @Autowired
//    protected RedisDSConfiguration redisConfiguration;
//
//    @Bean
//    public RedisDS createRedis() {
//        return RedisDS.create(redisConfiguration);
//    }

    public static void main(String[] args) {
        SpringApplication.run(LifpayAdminApplication.class, args);
        log.info("=======================Admin Application is run ....========================");
    }
}
