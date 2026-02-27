package org.hcm.lifpay.data;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@MapperScan("org.hcm.lifpay.data.dao.repository")
@Slf4j
public class LifpayDataApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(LifpayDataApplication.class, args);
        log.info("=======================Data Application is run ....========================");
    }
}
