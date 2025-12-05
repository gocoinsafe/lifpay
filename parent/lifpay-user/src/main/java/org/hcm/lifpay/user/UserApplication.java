package org.hcm.lifpay.user;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@MapperScan("org.hcm.lifpay.user.dao.mapper")
@Slf4j
public class UserApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        log.info("=======================user Application is run ....========================");
    }

}
